package com.latmn.kangaroo.framework.core.define;

public class Define {
    public static final String EMPTY_STR = "";
    public static final String TRACE_ID = "requestId";

    public static final String AUTH_HEADER_NAME = "apiKey";
    public static final String CONTENT_TYPE = "content-type";

    public static final String ERROR_CODE = "0";

    //登录失败统一返回
    public static final String AUTH_ERROR_CODE = "-001";
    public static final String PATH_ACCESS_NOT_POWER = "无权限访问此路径!";
    public static final String AUTH_ERROR_MESSAGE = "认证失败!";

    public static final String AUTH_TOKEN_ABSENT = "非法用户信息,此用户不存在,或被禁用,请重新登录!";
    public static final String SUCCESS_CODE = "1";

    public static final String CAPTCHA_NOT_EXIST = "验证码不存在!";
    public static final String CAPTCHA_NOT_INCORRECT = "验证码不正确!";

    public static final String ACCOUNT_OR_PASSWORD_ERROR = "账号或密码错误!";

    public static final String CACHE_CAPTCHA_PREFIX = "captcha-";
    public static final String CACHE_USER_PREFIX = "user-";
}
