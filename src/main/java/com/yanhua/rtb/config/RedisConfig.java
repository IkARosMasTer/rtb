/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: RedisConfig
 * Author: Emiya
 * Date: 2020/9/29 14:03
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *  <p>
 * @author Emiya
 * @create 2020/9/29 14:03
 * @version 1.0.0
 */
//xtends CachingConfigurerSupport
@Configuration
//@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{

    public final static String REDIS_PREFIX="rtb_" ; //redis前缀

//    @Bean
//     public RedisTemplate<String, Object> template(
//             RedisConnectionFactory connectionFactory) {
//         RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//         redisTemplate.setConnectionFactory(connectionFactory);
//         redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//         StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//         redisTemplate.setKeySerializer(stringRedisSerializer);
//         redisTemplate.setHashKeySerializer(stringRedisSerializer);
//         return redisTemplate;
//     }
//    @Bean
//    public StringRedisTemplate template2(RedisConnectionFactory factory) {
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
//        stringRedisTemplate.setConnectionFactory(factory);
//        return stringRedisTemplate;
//    }


    @Bean(name = "redisTemplateCustomize")
//    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        ObjectMapper mapper = new ObjectMapper();
        template.setConnectionFactory(connectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //LocalDateTime 类型用
//        mapper.registerModule(new ParameterNamesModule())
//                .registerModule(new Jdk8Module())
//                .registerModule(new JavaTimeModule());
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer( new StringRedisSerializer());
        //开启Redis事务
//        template.setEnableTransactionSupport(true);
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(serializer);
//
        template.setDefaultSerializer(serializer);
//        template.setEnableDefaultSerializer(true);
        template.afterPropertiesSet();
        System.out.println("========================redis开启成功！=====================");
        return template;
    }
//
//    //配置事务管理器,让Redis的事务由Translation控制
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) throws SQLException {
//        return new DataSourceTransactionManager(dataSource);
//    }

}