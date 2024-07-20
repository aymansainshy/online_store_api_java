package com.example.onlineStoreApi.services.cache;

import java.util.concurrent.ConcurrentHashMap;


public class InMemoryCache implements CacheService {

    private final ConcurrentHashMap<Long, String> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String data) {

    }

    @Override
    public String get(String data) {
        return "";
    }
}
