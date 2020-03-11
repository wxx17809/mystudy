
package com.ghkj.gaqweb.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${uploadImages}")
    private String uploadImages;

    @Override
    public  void addResourceHandlers(ResourceHandlerRegistry registry){
         registry.addResourceHandler("/img/**").addResourceLocations("file:"+uploadImages);
         super.addResourceHandlers(registry);
    }
}

