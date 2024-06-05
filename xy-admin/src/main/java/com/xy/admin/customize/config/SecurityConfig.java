package com.xy.admin.customize.config;

import cn.hutool.json.JSONUtil;
import com.xy.admin.common.cache.RedisCacheService;
import com.xy.admin.customize.async.AsyncTaskFactory;
import com.xy.admin.customize.securityService.TokenService;
import com.xy.admin.customize.securityService.UserDetailsServiceImpl;
import com.xy.common.base.BaseResponseData;
import com.xy.common.enums.common.LoginStatusEnum;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.infrastructure.thread.ThreadPoolManager;
import com.xy.infrastructure.user.web.SystemLoginUser;
import com.xy.infrastructure.utils.ServletHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * 主要配置登录流程逻辑涉及以下几个类
 *
 * @author valarchie
 * @see UserDetailsServiceImpl#loadUserByUsername  用于登录流程通过用户名加载用户
 * @see this#unauthorizedHandler()  用于用户未授权或登录失败处理
 * @see this#logOutSuccessHandler 用于退出登录成功后的逻辑
 * @see JwtAuthenticationTokenFilter#doFilter token的校验和刷新
// * @see LoginServiceImpl#login 登录逻辑
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenService tokenService;

    private final RedisCacheService redisCache;

    /**
     * token认证过滤器
     */
    private final JwtAuthenticationTokenFilter jwtTokenFilter;

    private final UserDetailsService userDetailsService;
    /**
     * 跨域过滤器
     */
    private final CorsFilter corsFilter;


    /**
     * 登录异常处理类
     * 用户未登陆的话  在这个Bean中处理
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, exception) -> {
            BaseResponseData<Object> baseResponseData = BaseResponseData.fail(
                    new ApiException(ErrorCode.Client.COMMON_NO_AUTHORIZATION, request.getRequestURI())
            );
            ServletHolderUtil.renderString(response, JSONUtil.toJsonStr(baseResponseData));
        };
    }
    /**
     * 退出成功处理类 返回成功
     * 在SecurityConfig类当中 定义了/logout 路径对应处理逻辑
     */
    @Bean
    public LogoutSuccessHandler logOutSuccessHandler() {
        return (request, response, authentication) -> {
            SystemLoginUser loginUser = tokenService.getLoginUser((HttpServletRequest) request);// 获取用户
            if (loginUser != null) {
                String userName = loginUser.getUsername();
                // 删除用户缓存记录
                redisCache.loginUserCache.delete(loginUser.getCachedKey());
                // 记录用户退出日志
                ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(
                        userName, LoginStatusEnum.LOGOUT, LoginStatusEnum.LOGOUT.description()));
            }
            ServletHolderUtil.renderString(response, JSONUtil.toJsonStr(BaseResponseData.ok()));
        };
    }


    /**
     * 鉴权管理类
     *
     * @see UserDetailsServiceImpl#loadUserByUsername
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);
        ProviderManager providerManager = new ProviderManager(provider);
        return providerManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);            // CSRF禁用，因为不使用session
        // 认证失败处理类
        http.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler()
        ));
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login", "/register", "/getConfig", "/captchaImage", "/api/**")
                .anonymous()
                .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                .requestMatchers("/swagger-ui.html").anonymous()
                .requestMatchers("/swagger-resources/**").anonymous()
                .requestMatchers("/webjars/**").anonymous()
                .requestMatchers("/*/api-docs", "/*/api-docs/swagger-config").anonymous()
                .requestMatchers("/**/api-docs.yaml").anonymous()
                .requestMatchers("/druid/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
        );
        // 禁用 X-Frame-Options 响应头。下面是具体解释：
        // X-Frame-Options 是一个 HTTP 响应头，用于防止网页被嵌入到其他网页的 <frame>、<iframe> 或 <object> 标签中，从而可以减少点击劫持攻击的风险
        http.headers((headersConfigurer) ->
                headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        // 登出配置
        http.logout((logout) -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessHandler(logOutSuccessHandler());
                }
        );
        // 添加JWT filter   需要一开始就通过token识别出登录用户 并放到上下文中   所以jwtFilter需要放前面
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS 过滤器filter
        http.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        http.addFilterBefore(corsFilter, LogoutFilter.class);
        return http.build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
