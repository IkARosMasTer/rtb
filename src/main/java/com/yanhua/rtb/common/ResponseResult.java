/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ResponseResult
 * Author: Emiya
 * Date: 2020/9/21 11:52
 * Description: 用来标记方法的返回值，是否需要包装
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.common;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 〈功能简述〉<br>
 * 〈用来标记方法的返回值，是否需要包装〉
 *  <p>
 * @author Emiya
 * @create 2020/9/21 11:52
 * @version 1.0.0
 */
@Retention(RUNTIME)
@Target({TYPE,METHOD})
@Mapping
@Documented
public @interface ResponseResult {

    /**
     * 入参是否解密，默认解密
     */
    boolean inDecode() default true;

    /**
     * 出参是否加密，默认加密
     */
    boolean outEncode() default true;

}