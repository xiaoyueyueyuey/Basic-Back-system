package com.xy.admin.controller;

import com.xy.admin.customize.securityService.CaptchaServiceImpl;
import com.xy.admin.dto.login.CaptchaDTO;
import com.xy.common.base.BaseResponseData;
import com.xy.infrastructure.annotations.ratelimit.RateLimit;
import com.xy.infrastructure.annotations.ratelimit.RateLimitKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "验证码API", description = "验证码相关接口")
@RestController
public class CaptchaController {
    @Resource
    private CaptchaServiceImpl captchaServiceImpl;

    /**
     * 生成验证码
     */
    @Operation(summary = "验证码")
    @RateLimit(key = RateLimitKey.LOGIN_CAPTCHA_KEY, time = 10, maxCount = 10, cacheType = RateLimit.CacheType.REDIS,
            limitType = RateLimit.LimitType.IP)
    @GetMapping("/captchaImage")
    public BaseResponseData<CaptchaDTO> getCaptchaImg() {
        CaptchaDTO captchaImg = captchaServiceImpl.generateCaptchaImg();
        return BaseResponseData.ok(captchaImg);
    }
}
