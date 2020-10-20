/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: SwaggerConfig
 * Author: Emiya
 * Date: 2020/9/29 14:05
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * 〈功能简述〉<br>
 * 〈〉
 *  <p>
 * @author Emiya
 * @create 2020/9/29 14:05
 * @version 1.0.0
 */
@EnableSwagger2
@Configuration
@ConditionalOnExpression("${swagger.enable:true}")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
//        ParameterBuilder ticketPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        ticketPar.name("token").description("登录校验token")//name表示对象名称，description表示描述
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build();//required表示是否必填，defaultvalue表示默认值
//        pars.add(ticketPar.build());//添加完此处一定要把下边的带***的也加上否则不生效

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yanhua.rtb.controller"))
                .paths(PathSelectors.any())
                .build();
//                .globalOperationParameters(pars);//************把消息头添加

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("rtb api")
                .description("视频彩铃业务后端接口")
                .termsOfServiceUrl("none")
                .version("1.0").build();
    }

}
