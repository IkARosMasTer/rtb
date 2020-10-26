/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: BusinessStatus
 * Author: Emiya
 * Date: 2020/9/21 14:49
 * Description: 全局业务状态码
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.common;

/**
 * 〈功能简述〉<br>
 * 〈全局业务状态码 枚举〉
 *  <p>
 * @author Emiya
 * @create 2020/9/21 14:49
 * @version 1.0.0
 */
public enum ResultCodeEnum {


    //--------------------------------------这些都是业务都可能发生的异常（预期中的异常）---------------------->w

    /**
     * 管理员 - 账号密码错误
     */
    ADMIN_PASSWORD(10101, "管理员账号密码错误"),

    /**
     * 未知错误
     */
    UNKNOWN(-1, "未知错误"),

    /**
     * 请求成功
     */
    SUCCESS(2000, "成功"),

    /**
     * 请求失败
     */
    FAIL(2001, "失败"),

    /**
     * 熔断请求
     */
    BREAKING(2002, "熔断"),

    /**
     * 非法请求
     */
    ILLEGAL_REQUEST(5000, "非法请求"),

    /**
     * 非法令牌
     */
    ILLEGAL_TOKEN(5008, "非法令牌"),

    /**
     * 其他客户登录
     */
    OTHER_CLIENTS_LOGGED_IN(5012, "其他客户登录"),

    /**
     * 令牌已过期
     */
    TOKEN_EXPIRED(5014, "令牌已过期"),

    /**
     * 参数错误：1001-1999；1000为自定义meg（为validate校验出的错误用）
     */
    PARAM_IS_INVALID(1001,"参数无效"),
    PARAM_IS_BLANK(1002,"参数为空"),
    PARAM_TYPE_BIND_ERROR(1003,"参数类型错误"),
    PARAM_NOT_COMPLETE(1004,"参数缺失"),
    PARAM_IS_INVALID_OR_BLANK(1005,"参数缺失或为空"),
    /**
     * 用户错误：3001-3999；3000为自定义msg
     */
    USER_NOT_LOGGED_IN(3001,"用户未登录"),
    USER_LOGIN_ERROR(3002,"账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(3003,"账号已被禁用"),
    USER_NOT_EXIST(3004,"用户不存在"),
    USER_HAS_EXIST(3005,"用户已存在"),
    ASYNC_TIMEOUT(3006,"后台处理中,请稍后查看");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(int code) {
        for (ResultCodeEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getMessage();
            }
        }
        return null;
    }
}