///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: CorsConfig
// * Author: Emiya
// * Date: 2020/9/30 15:15
// * Description:
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 〈功能简述〉<br>
// * 〈解决浏览器禁止不同源地址跨域访问：
// *      方案一：用@CrossOrigin，自身是一个拦截器，并且在springboot版本2.2前，cross拦截器是在自定义拦截器执行顺序之后，
// *      所以如果我们自定义的拦截器拦截失败则不会进入crossOrigin,新版boot已修复
// *      方案二：将跨域操作放到filter，filter执行比拦截器早。（自定义类继承CrosRegistration，
// *      并在webMVCConfig中增加filterRegistrationBean(),里面实现cross过滤器，并返回bean。这里没记录）
// *      原理：基于w3c的跨域协议，在请求需要跨域时，会额外发出一次OPTIONS（预检）请求已确认是否接受我的跨域请求，〉
// *  <p>
// * @author Emiya
// * @create 2020/9/30 15:15
// * @version 1.0.0
// */
////以下都是filter过滤器方式
//
////@Configuration
////public class CorsConfig {
////
////    // CORS
////    @Bean
////    FilterRegistrationBean corsFilter(@Value("*") String origin) {
////        return new FilterRegistrationBean(new Filter() {
////            @Override
////            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
////                    throws IOException, ServletException {
////                HttpServletRequest request = (HttpServletRequest) req;
////                HttpServletResponse response = (HttpServletResponse) res;
////                String method = request.getMethod();
////
////                response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
////                response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
////                response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
////                response.setHeader("Access-Control-Allow-Credentials", "true");
////                response.setHeader("Access-Control-Allow-Headers", "token,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
////                if ("OPTIONS".equals(method)) { //检测是options方法则直接返回200
////                    response.setStatus(HttpStatus.OK.value());
////                } else {
////                    chain.doFilter(req, res);
////                }
////            }
////            @Override
////            public void init(FilterConfig filterConfig) {
////            }
////
////            @Override
////            public void destroy() {
////            }
////        });
////    }
////
////}
//
//@Configuration
//public class CorsConfig {
//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        // 设置你要允许的网站域名，例如 localhost:8000,如果全允许则设为 *
//        config.addAllowedOrigin("*");
//        // 如果要限制 HEADER 或 METHOD 请自行更改
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
////        .allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(),
////                HttpMethod.PUT.name(), HttpMethod.OPTIONS.name())
////                .allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type",
////                        "Last-Modified", "device", "token")
////                .exposedHeaders(HttpHeaders.SET_COOKIE)
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        // 这个顺序很重要哦，为避免麻烦请设置在最前
//        bean.setOrder(0);
//        return bean;
//    }
//}