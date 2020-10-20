/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: MybatisPlusConfig
 * Author: Emiya
 * Date: 2020/9/28 18:15
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 〈功能简述〉<br>
 * 〈配置分页插件〉
 *  <p>
 * @author Emiya
 * @create 2020/9/28 18:15
 * @version 1.0.0
 */
@EnableTransactionManagement
@MapperScan("com.yanhua.rtb.mapper")
@Configuration
public class MybatisPlusConfig {

//    /**
//     * 性能优化  复杂查询可能会报错 建议关掉
//     * @return
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        /*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
//        performanceInterceptor.setMaxTime(1000);
//        /*<!--SQL是否格式化 默认false-->*/
//        performanceInterceptor.setFormat(false);
//        return performanceInterceptor;
//    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}