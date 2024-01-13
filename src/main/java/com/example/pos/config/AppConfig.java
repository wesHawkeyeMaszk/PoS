package com.example.pos.config;


import com.example.pos.services.ScanService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public ScanService scanService() {
        return new ScanService();
    }
}
