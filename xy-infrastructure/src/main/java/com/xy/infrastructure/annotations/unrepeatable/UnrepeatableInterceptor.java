package com.xy.infrastructure.annotations.unrepeatable;

import cn.hutool.json.JSONUtil;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.infrastructure.cache.RedisUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 重复提交拦截器 如果涉及前后端加解密的话  也可以通过继承RequestBodyAdvice来实现
 *
 * @author valarchie
 */
@ControllerAdvice(basePackages = "com.xy")//控制器通知类，用于全局处理请求。通过basePackages属性指定了需要拦截的包路径。
@Slf4j
@RequiredArgsConstructor
public class UnrepeatableInterceptor extends RequestBodyAdviceAdapter {//RequestBodyAdviceAdapter是Spring MVC提供的接口，用于处理请求体的适配器
    private final RedisUtil redisUtil;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType)
    {
        //supports方法用于判断当前方法是否有Unrepeatable注解，如果有，则返回true，表示支持对该方法进行处理。
        return methodParameter.hasMethodAnnotation(Unrepeatable1.class);
    }

    /**
     * @param body 仅获取有RequestBody注解的参数
     */
    @NotNull
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        // 仅获取有RequestBody注解的参数
        String currentRequest = JSONUtil.toJsonStr(body);
        Unrepeatable1 resubmitAnno = parameter.getMethodAnnotation(Unrepeatable1.class);//获取方法上的Unrepeatable注解，并根据注解中的checkType类型生成Redis key。
        if (resubmitAnno != null) {
            String redisKey = resubmitAnno.checkType().generateResubmitRedisKey(parameter.getMethod());//通过RedisUtil工具类从Redis中获取之前存储的请求参数
            log.info("请求重复提交拦截，当前key:{}, 当前参数：{}", redisKey, currentRequest);
            String preRequest = redisUtil.getCacheObject(redisKey);
            if (preRequest != null) {
                //判断当前请求参数与之前请求参数是否相同，如果相同则抛出ApiException异常，表示重复提交。
                boolean isSameRequest = Objects.equals(currentRequest, preRequest);
                if (isSameRequest) {
                    throw new ApiException(ErrorCode.Client.COMMON_REQUEST_RESUBMIT);
                }
            }
            redisUtil.setCacheObject(redisKey, currentRequest, resubmitAnno.interval(), TimeUnit.SECONDS);//将当前请求参数存入Redis中，以便后续判断是否重复提交。
        }
        return body;
    }

}
