/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ResponseResultHandler
 * Author: Emiya
 * Date: 2020/9/21 13:48
 * Description: 上面代码就是判断是否需要返回值包装，如果需要就直接包装。
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.common.Result;
import com.yanhua.rtb.common.ResultCodeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.MidiFileFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static com.yanhua.rtb.common.ResultCodeEnum.ASYNC_TIMEOUT;
import static com.yanhua.rtb.config.interceptor.ResponseResultInterceptor.RESPONSE_RESULT_ANN;


/**
 * 〈功能简述〉<br>
 * 〈上面代码就是判断是否需要返回值包装，如果需要就直接包装。〉
 *  <p>
 * @author Emiya
 * @create 2020/9/21 13:48
 * @version 1.0.0
 */
//@Order(1)
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    private static final Logger log = LogManager.getLogger(ResponseResultHandler.class);
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null){
            HttpServletRequest request = sra.getRequest();
            //判断请求是否有包装标记
            ResponseResult responseResult = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
            return responseResult != null;
        }
//        if (methodParameter.getMethod()!=null){
//            boolean ff =  methodParameter.getMethod().isAnnotationPresent(ResponseResult.class);
//            System.out.println(ff);
//            return ff;
//        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("全局统一结果封装=================>重写格式处理中。。。");
        if(!(body instanceof Result)) {
            //因为handler处理类的返回类型是String，为了保证一致性，这里需要将ResponseResult转回去
            if(body instanceof String) {
                body = Result.success(body);
//                private static final ObjectMapper objectMapper = new ObjectMapper()
//                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//                String str = objectMapper.writeValueAsString(new MessageEntity());
                return JSON.toJSONString(body);
            }
            body = Result.success(body);
        }
        return body;
//        return new ResponseEntity<>(bo, HttpStatus.OK);
//        return new ResponseEntity<>(bo, HttpStatus.OK);
        //为response的响应头修改返回类型。
//        response.getHeaders().setContentType(MediaType.parseMediaType(MediaType.TEXT_XML_VALUE));
//        response.getHeaders().setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        //也可以根据注解判断是否加密解密
    }


    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public <T>Result<T> handlerException(HttpServletRequest request, Throwable ex) {
        Result<T> result;
        //校验HTTP请求参数时的异常
        if (ex instanceof BindException) {
            result = new Result<>(1000);
            BindingResult bindingResult = ((BindException) ex).getBindingResult();
            if (bindingResult.hasErrors()) {
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                fieldErrors.forEach(fieldError -> {
                    //日志打印不符合校验的字段名和错误提示
                    log.error("----------------------->请求参数error field is : {} ,message is : {}", fieldError.getField(), fieldError.getDefaultMessage());
                });
                result = new Result<>(1000, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            // 校验HTTP请求参数时的异常
        } else if (ex instanceof MethodArgumentNotValidException) {
            result = new Result<>(1000);
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            if (bindingResult.hasErrors()) {
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                fieldErrors.forEach(fieldError -> {
                    //日志打印不符合校验的字段名和错误提示
                    log.error("----------------------->请求参数error field is : {} ,message is : {}", fieldError.getField(), fieldError.getDefaultMessage());
                });
                result = new Result<>(1000, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
            //HTTP请求异步执行超时则直接返回信息
        }else if (ex instanceof TimeoutException){
            result = new Result<>(3006,ex.getMessage());
            log.warn("---------------->异步任务提前返回信息");
            // 运行时程序全局异常
        }else if (ex instanceof EngineException) {
            result = Result.fail((EngineException) ex);
            log.warn("--------------->[全局业务异常]\r\n业务编码：{}\r\n异常记录：{}", result.getCode(), result.getMessage());
        // 连全局异常都未知的错误
        } else {
            log.error("---------------->全局之外的未知异常：{}",ex.getMessage());
            result = new Result<>(ResultCodeEnum.UNKNOWN.getCode(),ex);
        }
//        return new ResponseEntity<>(result, HttpStatus.OK);
        return result;
    }
}