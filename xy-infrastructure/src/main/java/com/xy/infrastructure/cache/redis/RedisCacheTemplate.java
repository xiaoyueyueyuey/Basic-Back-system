package com.xy.infrastructure.cache.redis;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xy.infrastructure.cache.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存模板类，实现了三级缓存机制
 * @param <T> 缓存对象类型
 * @author valarchie
 */
@Slf4j
public class RedisCacheTemplate<T> {

    // Redis工具类
    private final RedisUtil redisUtil;
    // Redis缓存键枚举
    private final CacheKeyEnum redisRedisEnum;
    // Guava缓存
    private final LoadingCache<String, Optional<T>> guavaCache;

    /**
     * 构造函数，初始化Redis缓存模板
     * @param redisUtil Redis工具类实例
     * @param redisRedisEnum Redis缓存键枚举
     */
    public RedisCacheTemplate(RedisUtil redisUtil, CacheKeyEnum redisRedisEnum) {
        this.redisUtil = redisUtil;
        this.redisRedisEnum = redisRedisEnum;
        // 使用Guava构建缓存
        this.guavaCache = CacheBuilder.newBuilder()
                // 最大容量
                .maximumSize(1024)
                // 使用软引用存储值
                .softValues()
                // 写入后5分钟过期
                .expireAfterWrite(redisRedisEnum.expiration(), TimeUnit.MINUTES)
                .concurrencyLevel(64)
                .initialCapacity(128)
                // 使用CacheLoader加载缓存值
                .build(new CacheLoader<String, Optional<T>>() {
                    @Override
                    public Optional<T> load(String cachedKey) {
                        // 从Redis获取缓存值
                        T cacheObject = redisUtil.getCacheObject(cachedKey);
                        log.debug("find the redis cache of key: {} is {}", cachedKey, cacheObject);
                        return Optional.ofNullable(cacheObject);
                    }
                });

    }

    /**
     * 根据ID从缓存中获取对象，如果缓存中没有则从DB获取
     * @param id 对象ID
     * @return 缓存中的对象
     */
    public T getObjectById(Object id) {
        String cachedKey = generateKey(id);
        try {
            Optional<T> optional = guavaCache.get(cachedKey);

            if (!optional.isPresent()) {
                T objectFromDb = getObjectFromDb(id);
                set(id, objectFromDb);
                return objectFromDb;
            }

            return optional.get();
        } catch (ExecutionException e) {
            log.error("Failed to get object from cache", e);
            return null;
        }
    }

    /**
     * 从缓存中获取对象，即使找不到的话也不从DB中找
     * @param id 对象ID
     * @return 缓存中的对象，或者null
     */
    public T getObjectOnlyInCacheById(Object id) {
        String cachedKey = generateKey(id);
        try {
            Optional<T> optional = guavaCache.get(cachedKey);
            log.debug("find the guava cache of key: {}", cachedKey);
            return optional.orElse(null);
        } catch (ExecutionException e) {
            log.error("Failed to get object from cache", e);
            return null;
        }
    }

    /**
     * 从缓存中直接获取对象，根据给定的Redis key
     * @param cachedKey Redis缓存键
     * @return 缓存中的对象，或者null
     */
    public T getObjectOnlyInCacheByKey(String cachedKey) {
        try {
            Optional<T> optional = guavaCache.get(cachedKey);
            log.debug("find the guava cache of key: {}", cachedKey);
            return optional.orElse(null);
        } catch (ExecutionException e) {
            log.error("Failed to get object from cache", e);
            return null;
        }
    }

    /**
     * 设置缓存对象到Redis和Guava缓存中
     * @param id 对象ID
     * @param obj 缓存对象
     */
    public void set(Object id, T obj) {
        redisUtil.setCacheObject(generateKey(id), obj, redisRedisEnum.expiration(), redisRedisEnum.timeUnit());
        guavaCache.refresh(generateKey(id));
    }

    /**
     * 根据ID删除缓存对象
     * @param id 对象ID
     */
    public void delete(Object id) {
        redisUtil.deleteObject(generateKey(id));
        guavaCache.refresh(generateKey(id));
    }

    /**
     * 刷新缓存对象的过期时间
     * @param id 对象ID
     */
    public void refresh(Object id) {
        redisUtil.expire(generateKey(id), redisRedisEnum.expiration(), redisRedisEnum.timeUnit());
        guavaCache.refresh(generateKey(id));
    }

    /**
     * 生成缓存键
     * @param id 对象ID
     * @return 缓存键
     */
    public String generateKey(Object id) {
        return redisRedisEnum.key() + id;
    }

    /**
     * 从数据库中获取对象
     * @param id 对象ID
     * @return 数据库中的对象
     */
    public T getObjectFromDb(Object id) {
        return null;
    }

}
