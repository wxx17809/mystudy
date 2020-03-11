package com.ghkj.gaqweb.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

@Configuration
public class Myconfig {

    @Value("${elasticIp}")
    private String elasticIp;
    @Value("${elasticUserPassword}")
    private String elasticUserPassword;

    @Bean
    public TransportClient client() throws Exception{

        Settings settings=Settings.builder()
                .put("cluster.name","elasticsearch")
                .put("xpack.security.user",elasticUserPassword)
                .build();

        TransportClient client=null;
        client=new PreBuiltXPackTransportClient(settings)
                  .addTransportAddress(new TransportAddress(InetAddress.getByName(elasticIp),9300));
        return client;
    }
}
