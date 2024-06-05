// 导入需要的包
package com.xy.admin.common.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.xy.admin.entity.SysPostEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.query.service.SysPostService;
import com.xy.admin.query.service.SysRoleService;
import com.xy.admin.query.service.SysUserService;
import com.xy.infrastructure.cache.RedisUtil;
import com.xy.infrastructure.cache.redis.CacheKeyEnum;
import com.xy.infrastructure.cache.redis.RedisCacheTemplate;
import com.xy.infrastructure.user.web.SystemLoginUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Redis缓存服务类
 * RedisUtil RedisCacheService RedisCacheTemplate 这三个类确实是共用一个 RedisUtil的 RedisTemplate 实例，
 * 而且每个 RedisCacheTemplate 对象都使用了同一个 RedisTemplate 实例来操作 Redis 缓存数据。
 * 这样可以确保所有的缓存操作都是基于同一个 Redis 连接池进行的，避免了多个连接池实例导致资源浪费和性能下降的问题。
 * 另外，每个 RedisCacheTemplate 对象内部都使用了 Guava 缓存来实现三级缓存机制，
 * 即在内存中维护了一个本地缓存，用于加速对缓存数据的访问。通过 Guava 缓存，可以在内存中快速查找缓存数据
 * 减少对 Redis 的频繁访问，提高缓存的命中率和性能。
 * <p>
 * 因此，每个 RedisCacheTemplate 对象都包含了一个 Guava 缓存对象，用于管理缓存数据的读取、写入和过期等操作。这
 * 样可以实现对不同类型的缓存对象进行管理，并且通过 Guava 缓存提供的功能来优化对缓存数据的访问和操作。
 * <p>
 * 根据上述代码的设计，RedisCacheTemplate 类主要是用来管理 Guava 缓存和 Redis 缓存之间的交互。
 * 如果没有使用 Guava 缓存，直接使用 Redis 缓存的话，就不需要对不同类型的缓存对象进行分类了。
 */
@Component
@RequiredArgsConstructor
public class RedisCacheService {
    private final RedisUtil redisUtil;
    public RedisCacheTemplate<String> unrepeatableTokenCache; // token缓存
    public RedisCacheTemplate<String> captchaCache; // 验证码缓存
    public RedisCacheTemplate<SystemLoginUser> loginUserCache; // 登录用户缓存
    public RedisCacheTemplate<SysUserEntity> userCache; // 用户缓存
    public RedisCacheTemplate<SysRoleEntity> roleCache; // 角色缓存
    public RedisCacheTemplate<SysPostEntity> postCache; // 岗位缓存

    //    依赖注入完成后执行初始化操作, 初始化缓存
    @PostConstruct
    public void init() {
        // 初始化验证码缓存
        captchaCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.CAPTCHAT);
        // 初始化登录用户缓存
        loginUserCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.LOGIN_USER_KEY);
        // 初始化用户缓存
        userCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.USER_ENTITY_KEY) {
            @Override
            public SysUserEntity getObjectFromDb(Object id) {
                SysUserService userService = SpringUtil.getBean(SysUserService.class);
                return userService.getById((Serializable) id);
            }
        };
        // 初始化角色缓存
        roleCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.ROLE_ENTITY_KEY) {
            @Override
            public SysRoleEntity getObjectFromDb(Object id) {
                SysRoleService roleService = SpringUtil.getBean(SysRoleService.class);
                return roleService.getById((Serializable) id);
            }
        };
        // 初始化岗位缓存
        postCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.POST_ENTITY_KEY) {
            @Override
            public SysPostEntity getObjectFromDb(Object id) {
                SysPostService postService = SpringUtil.getBean(SysPostService.class);
                return postService.getById((Serializable) id);
            }

        };
        // 初始化防重token缓存
        unrepeatableTokenCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.UNREPEATABLE_TOKEN_KEY);
        //        roleModelInfoCache = new RedisCacheTemplate<RoleInfo>(redisUtil, CacheKeyEnum.ROLE_MODEL_INFO_KEY) {
//            @Override
//            public RoleInfo getObjectFromDb(Object id) {
//                UserDetailsService userDetailsService = SpringUtil.getBean(UserDetailsService.class);
//                return userDetailsService.getRoleInfo((Long) id);
//            }
//
//        };

    }
}