package com.ghkj.gaqweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;



@SpringBootApplication(scanBasePackages = {"com.ghkj"})
@MapperScan("com.ghkj.gaqdao.mapper")
public class GaqWebApplication {

    public static void main(String[] args) {
        //集成Es必须加的，否则项目不能启动
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(GaqWebApplication.class, args);
    }

}
