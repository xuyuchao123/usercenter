package com.xyc.userc.service.impl;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.dao.CarNumOpenIdMapper;
import com.xyc.userc.dao.UserMapper;
import com.xyc.userc.service.RedisService;
import com.xyc.userc.util.RoleTypeEnum;
import com.xyc.userc.vo.CarNumInfoVo;
import com.xyc.userc.vo.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2020/12/8.
 */
@Service("redisService")
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class RedisServiceImpl implements RedisService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CarNumOpenIdMapper carNumOpenIdMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void storeUserInfoVo() throws Exception
    {
        LOGGER.info("进入存储用户信息至redis方法");
        List<UserInfoVo> userInfoVoList = userMapper.selectUserInfoVo(null);
        if(userInfoVoList == null || userInfoVoList.size() == 0)
        {
            return;
        }
        UserInfoVo tmpVo = userInfoVoList.get(0);
        String tmpOpenId = userInfoVoList.get(0).getOpenId();
//        String tmpRoleCode = tmpVo.getRoleCode();
        for(int i = 1; i < userInfoVoList.size(); i++)
        {
            UserInfoVo loopVo = userInfoVoList.get(i);
            if(tmpOpenId.equals(loopVo.getOpenId()))
            {
                String tmpRoleCode = tmpVo.getRoleCode();
                if(tmpRoleCode.indexOf("=") == -1)
                {
                    tmpRoleCode += "=";
                }
                tmpRoleCode += loopVo.getRoleCode();
                tmpRoleCode += "=";
                tmpVo.setRoleCode(tmpRoleCode);
                userInfoVoList.remove(i);
                i--;
            }
            else
            {
                String roleCode = tmpVo.getRoleCode();
                if(roleCode.charAt(roleCode.length()-1) == '=')
                {
                    roleCode = roleCode.substring(0,roleCode.length()-1);
                    tmpVo.setRoleCode(roleCode);
                }
                tmpVo = loopVo;
                tmpOpenId = loopVo.getOpenId();
            }
        }
        String lastRoleCode = tmpVo.getRoleCode();
        if(lastRoleCode.charAt(lastRoleCode.length()-1) == '=')
        {
            lastRoleCode = lastRoleCode.substring(0,lastRoleCode.length()-1);
            tmpVo.setRoleCode(lastRoleCode);
        }

        List<Map> list = carNumOpenIdMapper.selectCarNumInfo(null);
//        List KdyMobileList = null;
        if(list != null && list.size() > 0)
        {
            String curOpenId = null;
            int listIdx = 0;
            int listSize = list.size();
            Map curMap = null;
//            KdyMobileList = new ArrayList<String>();
            for (UserInfoVo userInfoVo : userInfoVoList)
            {
                //若角色列表包含开单员角色则收集其手机号到 KdyMobileList 中，方便统一查询对应的工号
//                if(userInfoVo.getRoleCode() != null && userInfoVo.getRoleCode().contains(RoleTypeEnum.ROLE_KDY.getRoleCode()))
//                {
//                    KdyMobileList.add(userInfoVo.getMobilePhone());
//                }
                //不管角色是不是开单员，都要收集手机号到 KdyMobileList 中，方便统一查询对应的工号
//                KdyMobileList.add(userInfoVo.getMobilePhone());
                curOpenId = userInfoVo.getOpenId();
                List<CarNumInfoVo> carNumInfoVoList = new ArrayList<>();
                while (listIdx < listSize)
                {
                    curMap = list.get(listIdx);
                    if(((String)curMap.get("OPENID")).equals(curOpenId))
                    {
                        CarNumInfoVo carNumInfoVo = new CarNumInfoVo();
                        carNumInfoVo.setCarNum(curMap.get("CARNUMBER").toString());
                        carNumInfoVo.setIsEnable(Integer.valueOf(curMap.get("IS_ENABLED").toString()));
                        carNumInfoVoList.add(carNumInfoVo);
                        listIdx++;
                    }
                    else
                    {
                        break;
                    }
                }
                userInfoVo.setCarNumList(carNumInfoVoList);
            }
        }
        // kdyMobileList 为空，表示 list 为空，用户手机号未开始收集
//        if(KdyMobileList == null)
//        {
//            KdyMobileList = new ArrayList<String>();
//            for (UserInfoVo userInfoVo : userInfoVoList)
//            {
//                //若角色列表包含开单员角色则收集其手机号到 KdyMobileList 中，方便统一查询对应的工号
////                if(userInfoVo.getRoleCode() != null && userInfoVo.getRoleCode().contains(RoleTypeEnum.ROLE_KDY.getRoleCode()))
////                {
////                    KdyMobileList.add(userInfoVo.getMobilePhone());
////                }
//                //不管角色是不是开单员，都要收集手机号到 KdyMobileList 中，方便统一查询对应的工号
//                KdyMobileList.add(userInfoVo.getMobilePhone());
//            }
//        }
        //开始按照手机号列表查询对应工号，并将手机号和工号的对应关系放到 mobileUserIdMap 中
        Map<String,String> mobileUserIdMap = new HashMap<>();
        List<Map> maps = userMapper.selectAllUserId();
        String tmpUserId = null;
        for(Map map : maps)
        {
            tmpUserId = (String)map.get("USER_ID");
            if(tmpUserId == null)
            {
                tmpUserId = "";
            }
            mobileUserIdMap.put(map.get("PHONE_NO").toString(),tmpUserId);
        }

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        String roleCode = null;
        for(UserInfoVo userInfoVo : userInfoVoList)
        {
            String gh = mobileUserIdMap.get(userInfoVo.getMobilePhone());
            if(gh == null)
            {
                gh = "";
            }
            userInfoVo.setGh(gh);

            roleCode = userInfoVo.getRoleCode();
            if(RoleTypeEnum.ROLE_SJ_0.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_SJ_1.getRoleCode().equals(roleCode) ||
                    roleCode.contains(RoleTypeEnum.ROLE_SJ_0.getRoleCode()) ||
                    roleCode.contains(RoleTypeEnum.ROLE_HLDD.getRoleCode()))
            {
                userInfoVo.setOperatorTime(null);
                userInfoVo.setLastStockCode(null);
//                userInfoVo.setGh(null);
            }
            else if(RoleTypeEnum.ROLE_JLY_KHB.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_JLY_BC.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_JLY_XC.getRoleCode().equals(roleCode) ||
                    RoleTypeEnum.ROLE_JLY_OTHER.getRoleCode().equals(roleCode))
            {
                userInfoVo.setCarNumList(null);
//                userInfoVo.setGh(null);
            }
            else if(roleCode.contains(RoleTypeEnum.ROLE_KDY.getRoleCode()))
            {
                userInfoVo.setCarNumList(null);
                userInfoVo.setOperatorTime(null);
                userInfoVo.setLastStockCode(null);
//                if(mobileUserIdMap.size() > 0)
//                {
//                    String gh = mobileUserIdMap.get(userInfoVo.getMobilePhone()).toString();
//                    userInfoVo.setGh(gh);
//                }
            }
            else
            {
                userInfoVo.setCarNumList(null);
                userInfoVo.setOperatorTime(null);
                userInfoVo.setLastStockCode(null);
//                userInfoVo.setGh(null);
            }
//            String json = JSON.toJSONString(userInfoVo);
//            String openId = userInfoVo.getOpenId();
//            redisTemplate.opsForValue().set(openId,json);
        }

        redisTemplate.executePipelined(new RedisCallback<List<Object>>()
        {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException
            {
                for (UserInfoVo userInfoVo : userInfoVoList)
                {
                    String key = userInfoVo.getOpenId();
                    connection.set(key.getBytes(),JSON.toJSONString(userInfoVo).getBytes());
                }
                return null;
            }
        });

        LOGGER.info("结束存储用户信息至redis方法");
    }


    //更新redis中用户信息及车牌号信息
    @Override
    public void updateRedis(String openId) throws Exception
    {
        List<UserInfoVo> userInfoVoList = userMapper.selectUserInfoVo(openId);
        //单个openid可能对应多个角色，需将角色编码统一汇总至一个userInfoVo中，用 "=" 分隔
        if(userInfoVoList != null && userInfoVoList.size() > 0)
        {
            if(userInfoVoList.size() > 1)
            {
                UserInfoVo tmpVo = userInfoVoList.get(0);
                String tmpRoleCode = tmpVo.getRoleCode();
                int size = userInfoVoList.size();
                for(int i = 1; i < size; i++)
                {
                    tmpRoleCode += "=";
                    tmpRoleCode += userInfoVoList.get(i).getRoleCode();
                }
                tmpVo.setRoleCode(tmpRoleCode);
                userInfoVoList.clear();
                userInfoVoList.add(tmpVo);
            }
            UserInfoVo userInfoVo = userInfoVoList.get(0);
            List<CarNumInfoVo> carNumInfoVoList = carNumOpenIdMapper.selectCarNumInfoVo(openId);
            if(carNumInfoVoList != null && carNumInfoVoList.size() > 0)
            {
                userInfoVo.setCarNumList(carNumInfoVoList);
            }
            else
            {
                userInfoVo.setCarNumList(new ArrayList<>());
            }
            //若角色为开单员则需获取其工号
//            if(userInfoVo.getRoleCode().equals(RoleTypeEnum.ROLE_KDY.getRoleCode()))
//            {
//                String gh = userMapper.selectUserId(userInfoVo.getMobilePhone());
//                userInfoVo.setGh(gh);
//            }
            String gh = userMapper.selectUserId(userInfoVo.getMobilePhone());
            userInfoVo.setGh(gh);
            String json = JSON.toJSONString(userInfoVo);
            redisTemplate.opsForValue().set(openId,json);
        }
    }
}
