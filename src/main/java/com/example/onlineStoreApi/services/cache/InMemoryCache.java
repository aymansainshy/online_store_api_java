package com.example.onlineStoreApi.services.cache;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service("InMemoryCache")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // By default, Spring beans are singleton scoped,
public class InMemoryCache implements CacheService {

    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String key, Object data) {
        cache.put(key, data);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public Boolean isHas(String key) {
        return cache.containsKey(key);
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }
}
