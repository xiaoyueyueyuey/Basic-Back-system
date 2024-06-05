package com.xy.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.xy.admin.customize.securityService.CaptchaServiceImpl;
import com.xy.admin.dto.login.ConfigDTO;
import com.xy.common.base.BaseResponseData;
import com.xy.infrastructure.annotations.ratelimit.RateLimit;
import com.xy.infrastructure.annotations.ratelimit.RateLimitKey;
import com.xy.infrastructure.config.XYBootConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统配置API", description = "系统配置相关接口")
@RestController
public class SysConfigurationController {
    @Resource
    private CaptchaServiceImpl captchaServiceImpl;
    @Resource
    private  XYBootConfig xyBootConfig;
    /**
     * 访问首页，提示语
     */
    @Operation(summary = "首页")
    @GetMapping("/")
    @RateLimit(key = RateLimitKey.TEST_KEY, time = 10, maxCount = 5, cacheType = RateLimit.CacheType.Map,
            limitType = RateLimit.LimitType.GLOBAL)
    public String index() {
        return StrUtil.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。",
                xyBootConfig.getName(), xyBootConfig.getVersion());
    }

    /**
     * 获取系统的内置配置
     *
     * @return 配置信息
     */
    @GetMapping("/getConfig")
    public BaseResponseData<ConfigDTO> getConfig() {
        ConfigDTO configDTO = captchaServiceImpl.getConfig();
        return BaseResponseData.ok(configDTO);
    }

}
