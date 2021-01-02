package com.rkc.zds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.rkc.zds.resource.config.FileStorageProperties;

@ServletComponentScan
@ComponentScan("com.rkc.zds")
@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({
    FileStorageProperties.class
})public class ResourceServerApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ResourceServerApp.class, args);
    }
    
}
