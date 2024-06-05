package com.xy.admin.customize.config;


import com.xy.admin.customize.securityService.TokenService;
import com.xy.infrastructure.user.AuthenticationUtils;
import com.xy.infrastructure.user.web.SystemLoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * token过滤器 验证token有效性
 * 继承OncePerRequestFilter类的话  可以确保只执行filter一次， 避免执行多次
 *
 * @author valarchie
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SystemLoginUser loginUser = tokenService.getLoginUser((javax.servlet.http.HttpServletRequest) request);//从请求中的token获取用户
        if (loginUser != null && AuthenticationUtils.getAuthentication() == null) {//如果用户不为空且当前用户未登录
            tokenService.refreshToken(loginUser);//刷新token
            // 如果没有将当前登录用户放入到上下文中的话，会认定用户未授权，返回用户未登陆的错误
            putCurrentLoginUserIntoContext(request, loginUser);
            log.debug("request process in jwt token filter. get login user id: {}", loginUser.getUserId());
        }
        //放行，如果用户已经存在，那么上下文就有用户了，后续的判断就会直接一路通过了
        chain.doFilter(request, response);
    }

    private void putCurrentLoginUserIntoContext(HttpServletRequest request, SystemLoginUser loginUser) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginUser,
                null, loginUser.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//这个操作的作用是将当前的 HTTP 请求信息（如请求的 IP 地址、会话 ID 等）存储到 UsernamePasswordAuthenticationToken 对象中的 details 属性中。
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
