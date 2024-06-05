package com.xy.infrastructure.annotations.unrepeatable;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.xy.infrastructure.cache.RedisUtil;
import com.xy.infrastructure.utils.ServletHolderUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class UnrepeatableAspect {

    @Pointcut("@annotation(unrepeatable)")
    public void unrepeatablePointcut(Unrepeatable unrepeatable) {
    }
    private final RedissonClient redissonClient;
    private final RedisUtil redisUtil;
    /**
     * 环绕通知, 围绕着方法执行
     * 两种方式
     * 方式一：加锁 固定时间内不能重复提交
     * 方式二：先请求获取token，这边再删除token,删除成功则是第一次提交
     */
    @Around("unrepeatablePointcut(unrepeatable)")
    public Object around(ProceedingJoinPoint joinPoint, Unrepeatable unrepeatable) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String serviceId = unrepeatable.serviceId();
        //用于记录成功或者失败
        boolean res = false;
        //防重提交类型
        String type = unrepeatable.limitType().name();
        if (type.equalsIgnoreCase(Unrepeatable.Type.PARAM.name())) {
            //方式一，参数形式防重提交
            //通过 redissonClient 获取分布式锁，基于IP地址、类名、方法名和服务ID生成唯一key
            long lockTime = unrepeatable.lockTime();
            String ipAddr = ServletUtil.getClientIP(ServletHolderUtil.getRequest());//获取IP地址
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();//获取方法
            String className = method.getDeclaringClass().getName();//获取类名
            String key = unrepeatable.serviceId() + ":repeat_submit:" +  DigestUtil.md5Hex(String.format("%s-%s-%s-%s", ipAddr, className, method, serviceId));//生成唯一key
            //使用 tryLock 尝试获取锁，如果无法获取（即锁已被其他请求持有），则认为是重复提交，直接返回null
            RLock lock = redissonClient.getLock(key);
            //锁自动过期时间为 lockTime 秒，确保即使程序异常也不会永久锁定资源,尝试加锁，最多等待0秒，上锁以后5秒自动解锁 [lockTime默认为5s, 可以自定义]
            res = lock.tryLock(0, lockTime, TimeUnit.SECONDS);

        } else {
            //方式二，令牌形式防重提交，这里写简单了，没有区分服务，IP啥的，就只有服务器生成随机token，返回给客户端，客户端提交时带上token，服务器验证token是否存在，存在则删除token
            //从请求头中获取 request-token，如果不存在，则抛出异常
            String requestToken = request.getHeader("request-token");
            if (StrUtil.isBlank(requestToken)) {
                throw new RuntimeException("请求未包含令牌");
            }
            //使用 request-token 和 serviceId 构造Redis的key，尝试从Redis中删除这个键。如果删除成功，说明是首次提交；否则认为是重复提交
//            String key = String.format(UnrepeatableKey.PREFIX, unrepeatable.serviceId(), requestToken);
            res = redisUtil.deleteObject(requestToken);
        }
        if (!res) {
            log.error("请求重复提交");
            return null;
        }
        //在环绕通知的前后记录日志，有助于跟踪方法执行情况和重复提交的检测
        log.info("环绕通知执行前");
        Object obj = joinPoint.proceed();
        log.info("环绕通知执行后");
        return obj;
    }


}
