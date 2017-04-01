package com.jackiez.base.common;

/**
 * Created by zsigui on 17-3-14.
 */

public final class ErrStatus {

    /**
     * 执行请求成功，请求正常时返回的状态
     */
    public static int SUCCESS = 0;

    // 77 未知错误情况，特殊错误码
    // 10001 ~ 19999 相关客户端请求出错（包括请求参数/网络状态等）
    // 20001 ~ 29999 相关用户信息出错（包括用户验证/会话超时等）
    // 30001 ~ 39999 相关服务端响应出错
    /**
     * 请求参数不合法，如格式错误
     */
    public static int ERR_C_PARAM_ILLEGAL = 10001;
    /**
     * 请求参数缺失，如参数为空
     */
    public static int ERR_C_PARAM_LACK = 10002;
    /**
     * 获取数据失败，如本地数据库/文件啥的
     */
    public static int ERR_C_GET_FAIL = 10003;
    /**
     * 网络连接失败
     */
    public static int ERR_C_NET_FAIL_CONN = 10011;
    public static String STR_ERR_C_NET_FAIL_CONN = "网络连接失败，请重试!";
    /**
     * 执行出错，如异常之类的导致未请求就失败了
     */
    public static int ERR_C_BAD_EXCUTE = 10012;
    /**
     * 返回数据错误格式错误
     */
    public static int ERR_C_BAD_CALLBACK_DATA = 10013;
    public static String STR_ERR_C_BAD_CALLBACK_DATA = "返回数据格式不支持，无法处理";
    /**
     * 用户验证不通过（未知账号/密码情况）
     */
    public static int ERR_U_VALID_FAIL = 20001;
    /**
     * 用户昵称不存在
     */
    public static int ERR_U_VALID_FAIL_NAME = 20002;
    /**
     * 用户密码错误
     */
    public static int ERR_U_VALID_FAIL_PWD = 20003;
    /**
     * 用户会话超时
     */
    public static int ERR_U_SESSION_TIMEOUT = 20004;
    /**
     * 同时登录多台设备
     */
    public static int ERR_U_MULTI_LOGIN = 20006;
    /**
     * 服务端验证参数不合法
     */
    public static int ERR_S_PARAM_ILLEGAL = 30001;
    public static String STR_ERR_S_PARAM_ILLEGAL = "参数不合法";
    /**
     * 服务端验证参数缺失
     */
    public static int ERR_S_PARAM_LACK = 10002;
    /**
     * 服务端请求方法错误
     */
    public static int ERR_S_METHOD_ILLEGAL = 10003;
    /**
     * 服务端获取信息失败
     */
    public static int ERR_S_GET_FAILED = 10004;
}
