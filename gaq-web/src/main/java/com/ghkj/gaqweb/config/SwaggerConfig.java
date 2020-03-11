package com.ghkj.gaqweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @version 1.0
 * @ClassName : SwaggerConfig
 * @Description TODO
 * @Author : 吴璇璇   http://localhost:8080/swagger-ui.html
 * @Date : 2019/8/29 10:03
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //需要扫描生成swagger文档接口的包路径，注意别写错了，错了swagger页面打开就不会有接口再上面
                .apis(RequestHandlerSelectors.basePackage("com.ghkj.gaqweb.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //api文档的一些页面基本信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot中使用Swagger2实现前后端分离开发")
                //作者的相关信息
                .contact(new Contact("吴璇璇", "https://blog.csdn.net/qq_34727675", ""))
                //版本号
                .version("1.0")
                //详细描述
                .description("接口文档")
                .build();
    }

}

