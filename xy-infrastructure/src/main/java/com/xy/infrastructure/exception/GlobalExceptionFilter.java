package com.xy.infrastructure.exception;


import cn.hutool.json.JSONUtil;
import com.xy.common.base.BaseResponseData;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode.Internal;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 异常过滤器，因为配置的全局异常捕获器只能捕获MVC框架的异常
 * 不能捕获filter的异常
 * @author valarchie
 */
@Slf4j
@WebFilter(filterName = "ExceptionFilter", urlPatterns = "/*")
public class GlobalExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (ApiException ex) {
            log.error("global filter exceptions", ex);
            String resultJson = JSONUtil.toJsonStr(BaseResponseData.fail(ex));
            writeToResponse(response, resultJson);
        } catch (Exception e) {
            log.error("global filter exceptions, unknown error:", e);
            BaseResponseData<Object> BaseResponseData = com.xy.common.base.BaseResponseData.fail(new ApiException(Internal.INTERNAL_ERROR, e.getMessage()));
            String resultJson = JSONUtil.toJsonStr(BaseResponseData);
            writeToResponse(response, resultJson);
        }
    }

    private void writeToResponse(ServletResponse response, String resultJson) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultJson);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
