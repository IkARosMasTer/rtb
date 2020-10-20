/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ResponseResultInterceptor
 * Author: Emiya
 * Date: 2020/9/21 11:55
 * Description: 拦截请求，是否此请求返回的值需要包装，其实就是运行的时候，解析@ResponseResult注解  image 此代码核心思想，就是获取此请求，是否需要返回值包装，设置一个属性标记。
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.config.interceptor;

import com.yanhua.rtb.common.ResponseResult;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 〈功能简述〉<br>
 * 〈拦截请求，是否此请求返回的值需要包装，其实就是运行的时候，解析@ResponseResult注解   此代码核心思想，就是获取此请求，是否需要返回值包装，设置一个属性标记。〉
 *  <p>
 * @author Emiya
 * @create 2020/9/21 11:55
 * @version 1.0.0
 */
@Component
public class ResponseResultInterceptor extends HandlerInterceptorAdapter {
    /**
     * 标记
     */
    public static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求的方法
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod)handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            //判断是否在类对象上面加了注解
            if (clazz.isAnnotationPresent(ResponseResult.class)) {
                //设置此请求返回体，需要包装，往下传递，在ResponseBodyAdvice接口进行判断
                request.setAttribute(RESPONSE_RESULT_ANN,clazz.getAnnotation(ResponseResult.class));
                //判断在方法上是否有该注解
            }else if (method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(RESPONSE_RESULT_ANN,method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
         System.out.println("请求响应完成");
    }
}