/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ResponseResult2
 * Author: Emiya
 * Date: 2020/9/21 14:42
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 〈功能简述〉<br>
 * 〈通用数据传输对象〉
 *  <p>
 * @author Emiya
 * @create 2020/9/21 14:42
 * @version 1.0.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 3468352004150968551L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回对象
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result() {
        super();
    }

    public Result(Integer code) {
        super();
        this.code = code;
    }

    public Result(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, Throwable throwable) {
        super();
        this.code = code;
        this.message = throwable.getMessage();
    }

    public Result(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public Result(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResultCodeEnum engineStatus){
        super();
        this.code = engineStatus.getCode();
        this.message = engineStatus.getMessage();
    }

    public Result(EngineException e){
        super();
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    /**
     * 默认成功会带有data
     * @param data
     */
    public static <T> Result<T> success(T data){
        Result<T> responseResult = success();
        responseResult.setData(data);
        return responseResult;
    }
    public static <T> Result<T> success(){
        return new Result<>(ResultCodeEnum.SUCCESS);
    }

    /**
     * 除了成功外的其他业务异常(预期内),只适用于接口中直接将自定义异常封装成result
     * @param engineStatus
     * @param <T>
     */
    public static <T> Result<T> result(ResultCodeEnum engineStatus){
        return new Result<>(engineStatus);
    }
    public static <T> Result<T> result(ResultCodeEnum engineStatus, T data){
        Result<T> responseResult = new Result<>(engineStatus);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 运行时程序报出的捕捉的全局异常(预料外)
     * @param e
     * @param data
     * @param <T>
     */
    public static <T> Result<T> fail(EngineException e, T data){
        Result<T> responseResult = new Result<>(e);
        responseResult.setData(data);
        return responseResult;
    }
    public static <T> Result<T> fail(EngineException e){
        return new Result<>(e);
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Result<?> other = (Result<?>) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        if (code == null) {
            return other.code == null;
        } else {
            return code.equals(other.code);
        }
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