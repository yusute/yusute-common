package com.yusute.common.response;

/**
 * @author yusutehot
 */
public enum AuthStateCode {
    @Alias("非法的请求参数")
    INVALID_REQUEST(10000, "Invalid request"),
    @Alias("用户认证失败")
    INVALID_CLIENT(10001, "Invalid client"),
    @Alias("非法的授权信息")
    INVALID_GRANT(10002, "Invalid grant"),
    @Alias("应用没有被授权，无法使用所指定的grant_type")
    UNAUTHORIZED_CLIENT(10003, "Unauthorized client"),
    @Alias("grant_type字段超过定义范围")
    UNSUPPORTED_GRANT_TYPE(10004, "Unsupported grant_type"),
    @Alias("scope信息无效或超出范围")
    INVALID_SCOPE(10005, "Invalid scope"),
    @Alias("提供的更新令牌已过期")
    EXPIRED_TOKEN(10006, "Expired token"),
    @Alias("redirect_uri字段与注册应用时所填写的不匹配")
    REDIRECT_URI_MISMATCH(10007, "Redirect_uri mismatch"),
    @Alias("response_type参数值超过定义范围")
    UNSUPPORTED_RESPONSE_TYPE(10008, "Unsupported response type"),
    @Alias("用户或授权服务器拒绝授予数据访问权限")
    ACCESS_DENIED(10009, "Access denied");

    private int code;
    private String description;

    AuthStateCode(int code, String description){
        this.code = code;
        this.description = description;
    }
}
