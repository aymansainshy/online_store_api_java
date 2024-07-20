package com.example.onlineStoreApi.core.config;

public class AppConfig {
    public String apiVersion  = "v1";
    public String apiPrefix = "api";


    private static AppConfig instance;

    // private constructor to prevent instantiation
    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (AppConfig.instance == null) AppConfig.instance = new AppConfig();
        return AppConfig.instance;
    }
}
