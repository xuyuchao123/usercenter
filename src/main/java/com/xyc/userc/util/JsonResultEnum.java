package com.xyc.userc.util;

/**
 * Created by 1 on 2020/6/29.
 */
public enum JsonResultEnum
{
    /* 成功 */
    SUCCESS(000, "成功"),

    /* 失败 */
    FAIL(999, "系统错误,操作失败!"),

    /* 参数错误：1000～1999 */
//    PARAM_NOT_VALID(1001, "参数无效"),
//    PARAM_IS_BLANK(1002, "参数为空"),
//    PARAM_TYPE_ERROR(1003, "参数类型错误"),
//    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN(100, "用户未登录"),
    USER_ACCOUNT_NOT_EXIST(101, "账号不存在"),
    USER_ACCOUNT_DISABLE(102, "账号不可用"),
    USER_ACCOUNT_LOCKED(103, "账号被冻结"),
    USER_PASSWORD_ERROR(104, "密码错误"),
    USER_PASSWORD_EXPIRED(105, "密码过期"),
    USER_INFO_NOT_EXIST(106,"用户信息不存在"),

    USER_MOBILE_NOT_EXIST(106,"手机号未注册"),
    USER_MESCODE_EXPIRED(107,"手机验证码过期"),
    USER_MESCODE_ERROR(108,"手机验证码错误"),
    USER_ACCOUNT_EXIST(109,"账号已存在"),
    USER_MOBILE_EXIST(110,"手机号已被注册"),
    USER_MESCODE_NOT_EXIST(111,"手机验证码为空"),
    USER_VERIFYCODE_ERROR(112,"验证码错误"),



//    USER_ACCOUNT_ALREADY_EXIST(007, "账号已存在"),
//    USER_ACCOUNT_USE_BY_OTHERS(008, "账号下线"),

    /* 业务错误 */

    CARNUM_NOT_BINDED(200, "用户未绑定该车牌号!"),
    CARNUM_BINDED(201, "车牌号已被绑定!"),
    CARNUM_NOT_EXIST(202, "车牌号不存在或已删除!"),
    CARNUM_ENABLED(203, "车牌号已在启用状态!"),
    CARNUM_BLACKLIST_EXIST(204,"车牌号已被拉黑!"),
    CARNUM_PARAM_NOT_EXIST(205,"车牌号参数为空！"),
    CARNUM_NOT_ENABLED(206,"车牌号未启用！"),

    MOBILE_NOT_BINDED(300, "手机号未绑定！"),
//    MOBILE_BLACKLIST_EXIST(301,"手机号已被拉黑!"),
    MOBILE_BINDED(302, "手机号已被绑定！"),

    PAGE_NOT_EXIST(400,"查询页数不能为空！"),
    SIZE_NOT_EXIST(401,"每页显示数目不能为空！"),

    NO_PERMISSION(500, "没有权限"),

    OPENID_NOT_EXIST(600,"openId不能为空！"),
    OPENID_UNDEFINED(601,"openId为undefined！"),
    OPENID_BINDED(602,"openId已被绑定！"),

    FILE_NOT_EXIST(700,"上传的文件不能为空！"),
    FILE_EXIST(701,"上传的文件已存在"),

    SHIPINFO_CHECK_FAIL(800,"船户信息审核失败！"),

    ROLE_NOT_EXIST(900,"角色不存在或已删除！"),

    GARAGEINFO_NOT_EXIST(1000,"库位配置信息不存在！"),
    LOCATION_NOT_EXIST(1001,"库区信息不存在！"),

    BIGLADINGBILLNO_NOT_EXIST(1100,"提单号不存在！"),

    QRCODE_EXPIRED(1200,"二维码过期！"),

    VIOLATIONINFO_EXIST(1300,"违章信息已存在！");



    private Integer code;
    private String message;

    JsonResultEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取message
     */
    public static String getMessageByCode(Integer code)
    {
        for (JsonResultEnum resultEnum : values())
        {
            if (resultEnum.getCode().equals(code))
            {
                return resultEnum.getMessage();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
