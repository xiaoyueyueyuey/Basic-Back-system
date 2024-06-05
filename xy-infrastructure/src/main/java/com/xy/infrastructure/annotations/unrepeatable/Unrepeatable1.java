package com.xy.infrastructure.annotations.unrepeatable;

import cn.hutool.core.util.StrUtil;
import com.xy.infrastructure.user.AuthenticationUtils;
import com.xy.infrastructure.user.app.AppLoginUser;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * 自定义注解防止表单重复提交
 * 仅生效于有RequestBody注解的参数  因为使用RequestBodyAdvice来实现
 * @author valarchie
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unrepeatable1 {
    /**
     * 间隔时间(s)，小于此时间视为重复提交
     */
    int interval() default 5;
    // TODO改成和rate limit一样   可以选择类型
    /**
     * 检测条件类型
     */
    CheckType checkType() default CheckType.SYSTEM_USER;
    @Slf4j
    enum CheckType {
        /**
         * 按App用户
         */
        APP_USER {
            @Override
            public String generateResubmitRedisKey(Method method) {
                String username;

                try {
                    AppLoginUser loginUser = AuthenticationUtils.getAppLoginUser();
                    username = loginUser.getUsername();
                } catch (Exception e) {
                    username = NO_LOGIN;
                    log.error("could not find the related user to check repeatable submit.");
                }
//                使用Hutool工具类的StrUtil.format方法，按照指定的格式生成一个Redis key。
//                格式中包含了类型（即SYSTEM_USER）、方法所在类的名称、方法的名称和用户名
                return StrUtil.format(RESUBMIT_REDIS_KEY,
                    this.name(),
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    username);

            }
        },
        /**
         * 按Web用户
         */
        SYSTEM_USER {
            @Override
            public String generateResubmitRedisKey(Method method) {
                String username;
                try {
                    SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();//获取当前登录用户
                    username = loginUser.getUsername();//赋值给变量username
                } catch (Exception e) {
                    username = NO_LOGIN;
                    log.error("could not find the related user to check repeatable submit.");
                }
                return StrUtil.format(RESUBMIT_REDIS_KEY,
                    this.name(),
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    username);
            }
        };

        public static final String NO_LOGIN = "Anonymous";
        public static final String RESUBMIT_REDIS_KEY = "resubmit:{}:{}:{}:{}";

        public abstract String generateResubmitRedisKey(Method method);

    }

}
