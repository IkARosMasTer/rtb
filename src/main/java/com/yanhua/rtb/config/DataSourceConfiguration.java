///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: DataSourceConfiguration
// * Author: Emiya
// * Date: 2020/10/21 10:38
// * Description: 数据源（包含连接池）配置
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.util.StringUtils;
//
//import javax.sql.DataSource;
//
///**
// * 〈功能简述〉<br>
// * 〈数据源（包含连接池）配置〉
// *  <p>
// * @author Emiya
// * @create 2020/10/21 10:38
// * @version 1.0.0
// */
//@Configuration
//public class DataSourceConfiguration {
//
//    /**
//     * 创建 主 数据源的配置对象
//     */
//    @Primary
//    @Bean(name = "primaryDataSourceProperties")
//    @ConfigurationProperties(prefix = "spring.datasource.primary") // 读取 spring.datasource.primary 配置到 DataSourceProperties 对象
//    public DataSourceProperties primaryDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    /**
//     * 创建 主 数据源
//     */
//    @Primary
//    @Bean(name = "primaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari") // 读取 spring.datasource.primary 配置到 HikariDataSource 对象
//    public DataSource primaryDataSource() {
//        // 获得 DataSourceProperties 对象
//        DataSourceProperties properties = this.primaryDataSourceProperties();
//        // 创建 HikariDataSource 对象
//        return createHikariDataSource(properties);
//    }
//
//    /**
//     * 创建 quartz 数据源的配置对象
//     */
//    @Bean(name = "quartzDataSourceProperties")
//    @ConfigurationProperties(prefix = "spring.datasource.quartz") // 读取 spring.datasource.quartz 配置到 DataSourceProperties 对象
//    public DataSourceProperties quartzDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    /**
//     * 创建 quartz 数据源
//     */
//    @Bean(name = "quartzDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.quartz.hikari")
//    @QuartzDataSource
//    public DataSource quartzDataSource() {
//        // 获得 DataSourceProperties 对象
//        DataSourceProperties properties = this.quartzDataSourceProperties();
//        // 创建 HikariDataSource 对象
//        return createHikariDataSource(properties);
//    }
//
//    private static HikariDataSource createHikariDataSource(DataSourceProperties properties) {
//        // 创建 HikariDataSource 对象
//        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//        // 设置线程池名
//        if (StringUtils.hasText(properties.getName())) {
//            dataSource.setPoolName(properties.getName());
//        }
//        return dataSource;
//    }
//}