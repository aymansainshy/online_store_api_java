package com.example.onlineStoreApi.services.cache;


import org.springframework.stereotype.Service;

@Service("RedisCache")
public class RedisCache implements CacheService {


    @Override
    public void put(String key, Object data) {

    }

    @Override
    public String get(String key) {
        return "";
    }

    @Override
    public Boolean isHas(String key) {
        return false;
    }

    @Override
    public void delete(String key) {

    }
}
