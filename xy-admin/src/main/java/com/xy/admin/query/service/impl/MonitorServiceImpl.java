package com.xy.admin.query.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xy.admin.common.cache.CacheCenter;
import com.xy.admin.dto.monitor.OnlineUserDTO;
import com.xy.admin.dto.monitor.RedisCacheInfoDTO;
import com.xy.admin.dto.monitor.ServerInfo;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.infrastructure.cache.redis.CacheKeyEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 监控服务实现类，提供了获取Redis缓存信息、获取在线用户列表和获取服务器信息的功能
 */
@Service
@RequiredArgsConstructor
public class MonitorServiceImpl {

    private final RedisTemplate<String, ?> redisTemplate;

    /**
     * 获取Redis缓存信息，包括Redis信息、命令统计和数据库大小等
     *
     * @return Redis缓存信息DTO对象
     * @throws ApiException 当获取Redis监控信息失败时抛出异常
     */
    public RedisCacheInfoDTO getRedisCacheInfo() {
        // 获取Redis信息
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        // 获取Redis命令统计信息
        Properties commandStats = (Properties) redisTemplate.execute(
                (RedisCallback<Object>) connection -> connection.info("commandstats"));
        // 获取Redis数据库大小
        Long dbSize = redisTemplate.execute(RedisServerCommands::dbSize);

        // 如果获取的信息为空，抛出异常
        if (commandStats == null || info == null) {
            throw new ApiException(ErrorCode.Internal.INTERNAL_ERROR, "获取Redis监控信息失败。");
        }

        // 构建Redis缓存信息DTO对象
        RedisCacheInfoDTO cacheInfo = new RedisCacheInfoDTO();
        cacheInfo.setInfo(info);
        cacheInfo.setDbSize(dbSize);
        cacheInfo.setCommandStats(new ArrayList<>());

        // 解析命令统计信息，并添加到缓存信息DTO对象中
        commandStats.stringPropertyNames().forEach(key -> {
            String property = commandStats.getProperty(key);
            RedisCacheInfoDTO.CommandStatusDTO commandStatus = new RedisCacheInfoDTO.CommandStatusDTO();
            commandStatus.setName(StrUtil.removePrefix(key, "cmdstat_"));
            commandStatus.setValue(StrUtil.subBetween(property, "calls=", ",usec"));

            cacheInfo.getCommandStats().add(commandStatus);
        });

        return cacheInfo;
    }

    /**
     * 获取在线用户列表，可以根据用户名和IP地址进行过滤
     *
     * @param username 用户名（可选）
     * @param ipAddress IP地址（可选）
     * @return 在线用户列表
     */
    public List<OnlineUserDTO> getOnlineUserList(String username, String ipAddress) {
        // 获取所有在线用户的缓存键
        Collection<String> keys = redisTemplate.keys(CacheKeyEnum.LOGIN_USER_KEY.key() + "*");

        // 构建在线用户流
        Stream<OnlineUserDTO> onlineUserStream = keys.stream().map(key ->
                        CacheCenter.loginUserCache.getObjectOnlyInCacheByKey(key))
                .filter(Objects::nonNull).map(OnlineUserDTO::new);

        // 根据用户名和IP地址进行过滤
        List<OnlineUserDTO> filteredOnlineUsers = onlineUserStream
                .filter(user ->
                        StrUtil.isEmpty(username) || username.equals(user.getUsername())
                ).filter( user ->
                        StrUtil.isEmpty(ipAddress) || ipAddress.equals(user.getIpAddress())
                ).collect(Collectors.toList());

        // 反转列表，使最近登录的用户在前面
        Collections.reverse(filteredOnlineUsers);
        return filteredOnlineUsers;
    }

    /**
     * 获取服务器信息，包括操作系统、Java虚拟机、内存和CPU等信息
     *
     * @return 服务器信息对象
     */
    public ServerInfo getServerInfo() {
        return ServerInfo.fillInfo();
    }

}
