/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: RestTemplateConfig
 * Author: Emiya
 * Date: 2020/11/9 10:48
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *  <p>
 * @author Emiya
 * @create 2020/11/9 10:48
 * @version 1.0.0
 */
@Configuration
public class RestTemplateConfig {


    /**
     * 方式一: 默认是使用JDK原生java.net.HttpURLConnection请求
     * @return
     */
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}