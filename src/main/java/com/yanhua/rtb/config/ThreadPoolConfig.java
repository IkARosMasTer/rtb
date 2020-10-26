///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: TreadPoolConfig
// * Author: Emiya
// * Date: 2020/10/26 11:36
// * Description:
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb.config;
//
//import com.yanhua.rtb.common.EngineException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * 〈功能简述〉<br>
// * 〈异步线程的线程池，线程若异常则不知道能不能用全局来抛出〉
// *  <p>
// * @author Emiya
// * @create 2020/10/26 11:36
// * @version 1.0.0
// */
//
//@Configuration
//@EnableAsync
//@Slf4j
//public class ThreadPoolConfig implements AsyncConfigurer {
//
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        //核心线程池数量，方法: 返回可用处理器的Java虚拟机的数量。
//        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
//        //最大线程数量
//        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors()*5);
//        //线程池的队列容量
//        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors()*2);
//        //线程名称的前缀
//        executor.setThreadNamePrefix("this-excutor-");
//        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
//        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
//        executor.initialize();
//        log.info("--------------------------》》》开启异步线程池");
//        return executor;
//    }
//    /*异步任务中异常处理*/
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//
//        return new MyAsyncExceptionHandler();
//    }
//
//    /**
//     * 自定义异常处理类
//     * @author hry
//     *
//     */
//    static class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
//
//        //手动处理捕获的异常
//
//        @Override
//        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
//            System.out.println("-------------》》》捕获线程异常信息");
//            log.info("Exception message - " + throwable.getMessage());
//            log.info("Method name - " + method.getName());
//            for (Object param : objects) {
//                log.info("Parameter value - " + param);
//            }
//            //这里异步线程报错应该无法抛出全局异常，因为http请求已经提前返回了。
//            throw new EngineException("vv");
//        }
//    }
//}