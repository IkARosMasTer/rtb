/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: BusinessException
 * Author: Emiya
 * Date: 2020/9/21 14:47
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.common;

/**
 * 〈功能简述〉<br>
 * 〈全局业务异常〉
 *  <p>
 * @author Emiya
 * @create 2020/9/21 14:47
 * @version 1.0.0
 */
public class EngineException extends RuntimeException{

    private static final long serialVersionUID = 3034121940056795549L;

    //运行时service和dao层抛出的直接异常的默认code
    private Integer code = 4000;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public EngineException() {

    }

    public EngineException(String message) {
        super(message);
    }
    public EngineException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public EngineException(ResultCodeEnum status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public EngineException(ResultCodeEnum status, Throwable e) {
        super(status.getMessage(),e);
        this.code = status.getCode();
    }
}