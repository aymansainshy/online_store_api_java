package com.example.onlineStoreApi.services.cache;

public interface CacheService {
    void put(String key, Object data);

    Object get(String key);

    Boolean isHas(String key);

    void delete(String key);
}
