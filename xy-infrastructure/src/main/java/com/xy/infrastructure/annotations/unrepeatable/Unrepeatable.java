package com.xy.infrastructure.annotations.unrepeatable;

import java.lang.annotation.*;

@Inherited// 允许子类继承父类的注解
@Target(ElementType.METHOD)// 用于描述注解的使用范围
@Retention(RetentionPolicy.RUNTIME)// 用于描述注解的生命周期
@Documented// 用于描述其它类型的annotation应该被作为被标注的程序成员的公共API
public @interface Unrepeatable {
    /**
     * 定义了两种防止重复提交的方式，PARAM 表示基于方法参数来防止重复，TOKEN 则可能涉及生成和验证token的机制
     */
    enum Type { PARAM, TOKEN }
    /**
     * 设置默认的防重提交方式为基于方法参数。开发者可以不指定此参数，使用默认值。
     * @return Type
     */
    Type limitType() default Type.PARAM;
    /**
     * 允许设置加锁的过期时间，默认为5秒。这意味着在第一次请求之后的5秒内，相同的请求将被视为重复并被阻止
     * @return
     */
    long lockTime() default 5;
    //提供了一个可选的服务ID参数，通过token时用作KEY计算
    String serviceId() default "";

}
