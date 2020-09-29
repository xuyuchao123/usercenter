package com.xyc.userc.util;

import com.alibaba.fastjson.JSON;
import com.xyc.userc.controller.TemplateController;
import com.xyc.userc.service.UserService;
import com.xyc.userc.vo.CarNumInfoVo;
import com.xyc.userc.vo.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2020/9/26.
 * 项目启动时执行，将绑定的所有用户信息存入redis
 */
@Component
@Order
public class MyCommandLineRunner implements CommandLineRunner
{

    protected static final Logger LOGGER = LoggerFactory.getLogger(MyCommandLineRunner.class);

    @Resource
    UserService userService;

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    //启动s任务的核心逻辑，当项目启动时，run方法会被自动执行。
    @Override
    public void run(String... args) throws Exception
    {
        LOGGER.info("开始存入用户信息至redis");
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setValueSerializer(RedisSerializer.json());
//        Object obj = (String)redisTemplate.opsForValue().get("tst");
//        userService.storeUserInfoVo();

//        List<UserInfoVo> userInfoVoList = new ArrayList<>();
//
//        UserInfoVo userInfoVo = new UserInfoVo();
//        userInfoVo.setOpenId("asdfjkl");
//        userInfoVo.setMobilePhone("13988888");
//        userInfoVo.setRoleCode("SJ0");
//
//        List<CarNumInfoVo> carNumInfoVoList = new ArrayList<>();
//        CarNumInfoVo carNumInfoVo = new CarNumInfoVo();
//        carNumInfoVo.setCarNum("苏E88888");
//        carNumInfoVo.setIsEnable(1);
//        CarNumInfoVo carNumInfoVo2 = new CarNumInfoVo();
//        carNumInfoVo2.setCarNum("苏E99999");
//        carNumInfoVo2.setIsEnable(0);
//        carNumInfoVoList.add(carNumInfoVo);
//        carNumInfoVoList.add(carNumInfoVo2);
//
//        userInfoVo.setCarNumList(carNumInfoVoList);
//        String json = JSON.toJSONString(userInfoVo);
//        System.out.println(json);
//        redisTemplate.opsForValue().set("asdfjkl",json);
//        System.out.println(redisTemplate.opsForValue().get("asdfjkl"));
        LOGGER.info("结束存入用户信息至redis");
    }

}
