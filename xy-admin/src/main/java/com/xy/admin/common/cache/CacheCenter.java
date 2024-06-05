package com.xy.admin.common.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.xy.admin.entity.SysDeptEntity;
import com.xy.admin.entity.SysPostEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.infrastructure.cache.guava.AbstractGuavaCacheTemplate;
import com.xy.infrastructure.cache.redis.RedisCacheTemplate;
import com.xy.infrastructure.user.web.SystemLoginUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * 缓存中心  提供全局访问点
 * 如果是领域类的缓存  可以自己新建一个直接放在CacheCenter   不用放在infrastructure包里的GuavaCacheService
 * 或者RedisCacheService
 * @author valarchie
 */
@Component
public class CacheCenter {

    public static AbstractGuavaCacheTemplate<String> configCache;

    public static AbstractGuavaCacheTemplate<SysDeptEntity> deptCache;

    public static RedisCacheTemplate<String> captchaCache;

    public static RedisCacheTemplate<SystemLoginUser> loginUserCache;

    public static RedisCacheTemplate<SysUserEntity> userCache;

    public static RedisCacheTemplate<SysRoleEntity> roleCache;

    public static RedisCacheTemplate<SysPostEntity> postCache;
    public static RedisCacheTemplate<String> unrepeatableTokenCache;

    @PostConstruct
    public void init() {
        GuavaCacheService guavaCache = SpringUtil.getBean(GuavaCacheService.class);
        RedisCacheService redisCache = SpringUtil.getBean(RedisCacheService.class);

        configCache = guavaCache.configCache;
        deptCache = guavaCache.deptCache;
        captchaCache = redisCache.captchaCache;
        loginUserCache = redisCache.loginUserCache;
        userCache = redisCache.userCache;
        roleCache = redisCache.roleCache;
        postCache = redisCache.postCache;
        unrepeatableTokenCache = redisCache.unrepeatableTokenCache;
    }

}
