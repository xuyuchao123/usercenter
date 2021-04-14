package com.xyc.userc.controller;

import com.xyc.userc.service.FreezeService;
import com.xyc.userc.util.CommonExceptionHandler;
import com.xyc.userc.util.JsonResultObj;
import com.xyc.userc.util.JsonResultObj_Page;
import com.xyc.userc.vo.CarNumFrozenAddVo;
import com.xyc.userc.vo.CarNumFrozenQueryVo;
import com.xyc.userc.vo.CarNumFrozenVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by 1 on 2021/4/13.
 */
@RestController
@CrossOrigin
@RequestMapping("/freeze")
@Api(tags = "pc端违章冻结相关api")
public class PcFreezeController
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(PcFreezeController.class);

    @Resource
    FreezeService freezeService;

    @PostMapping("/querycarnumfrozen")
    @ApiOperation(value="查询车牌号违章冻结信息")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：查询成功，isSuccess=false：查询失败，resMsg为错误信息")})
    public JsonResultObj_Page<CarNumFrozenVo> queryCarNumFrozen(@Validated CarNumFrozenQueryVo vo)
    {
        LOGGER.info("开始查询车牌号违章冻结信息 {}",vo.toString());
        JsonResultObj_Page jsonResultObjPage = null;
        try
        {
            List resList = freezeService.queryCarNumFrozen(vo.getCarNum(),vo.getFrozenStatus(),vo.getPage(),vo.getSize());
            List<CarNumFrozenVo> carNumFrozenVos = null;
            String total = null;
            String page = null;
            String size = null;
            if(resList != null && resList.size() == 4)
            {
                total = resList.get(0).toString();
                page = resList.get(1).toString();
                size = resList.get(2).toString();
                carNumFrozenVos = (List<CarNumFrozenVo>)resList.get(3);
            }
            jsonResultObjPage = new JsonResultObj_Page(true,carNumFrozenVos,total,page,size);
        }
        catch (Exception e)
        {
            jsonResultObjPage = CommonExceptionHandler.handException_page(e, "查询车牌号违章冻结信息失败", LOGGER);
        }
        LOGGER.info("结束查询车牌号违章冻结信息 {}",vo.toString());
        return jsonResultObjPage;
    }


    @PostMapping(value="/freezecarnum",consumes = "application/json")
    @ApiOperation(value="冻结车牌号")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：冻结成功，isSuccess=false：冻结失败，resMsg为错误信息")})
    public JsonResultObj freezeCarNum(@Validated @NotNull(message="车牌号列表不能为空") @RequestBody List<CarNumFrozenAddVo> vos)
    {
        LOGGER.info("开始冻结车牌号 CarNumFrozenAddVos={}",vos.toString());
        JsonResultObj jsonResultObj = null;
        try
        {
            freezeService.freezeCarNum(vos);
            jsonResultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            jsonResultObj = CommonExceptionHandler.handException(e, "冻结车牌号失败", LOGGER);
        }
        LOGGER.info("结束冻结车牌号 CarNumFrozenAddVos={}",vos.toString());
        return jsonResultObj;
    }

    @PostMapping(value="/unfreezecarnum")
    @ApiOperation(value="解冻车牌号")
    @ApiImplicitParam(name="carNum", value="车牌号", required=true, allowMultiple=true, dataType = "String")
    @ApiResponses({@ApiResponse(code = 200,  message = "isSuccess=true：解冻成功，isSuccess=false：解冻失败，resMsg为错误信息")})
    public JsonResultObj<CarNumFrozenVo> unFreezeCarNum(@NotNull(message = "车牌号列表不能为空") @RequestParam("carNum")List<String> carNums)
    {
        LOGGER.info("开始解冻车牌号 carNums={}",carNums.toArray().toString());
        JsonResultObj jsonResultObj = null;
        try
        {
            freezeService.unFreezeCarNum(carNums);
            jsonResultObj = new JsonResultObj(true);
        }
        catch (Exception e)
        {
            jsonResultObj = CommonExceptionHandler.handException(e, "冻结车牌号失败", LOGGER);
        }
        LOGGER.info("结束解冻车牌号 carNums={}",carNums.toArray().toString());
        return jsonResultObj;
    }
}
