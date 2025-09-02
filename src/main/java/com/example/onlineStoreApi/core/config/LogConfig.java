package com.example.onlineStoreApi.core.config;


import jakarta.annotation.PostConstruct;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogConfig {

    private final String appName;

    public LogConfig(@Value("${app.name}") String appName) {
        this.appName = appName;
    }

    @PostConstruct
    public void  init(){
        MDC.put("app_name", appName);
    }
}
