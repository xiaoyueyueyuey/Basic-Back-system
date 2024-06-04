package com.xy.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.xy.admin.common.dto.common.CurrentLoginUserDTO;
import com.xy.admin.common.dto.common.TokenDTO;
import com.xy.admin.customize.service.LoginService;
import com.xy.admin.dto.login.CaptchaDTO;
import com.xy.admin.dto.login.ConfigDTO;
import com.xy.admin.dto.menu.RouterDTO;
import com.xy.admin.query.service.SysMenuService;
import com.xy.admin.query.service.application.UserApplicationService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.system.login.LoginCommand;
import com.xy.domain.system.user.command.manager.AddUserCommand;
import com.xy.infrastructure.annotations.ratelimit.RateLimit;
import com.xy.infrastructure.annotations.ratelimit.RateLimitKey;
import com.xy.infrastructure.config.XYBootConfig;
import com.xy.infrastructure.user.AuthenticationUtils;
import com.xy.infrastructure.user.web.SystemLoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页
 *
 * @author valarchie
 */
@Tag(name = "登录API", description = "登录相关接口")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final SysMenuService menuApplicationService;

    private final UserApplicationService userApplicationService;

    private final XYBootConfig xyBootConfig;

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
        ConfigDTO configDTO = loginService.getConfig();
        return BaseResponseData.ok(configDTO);
    }

    /**
     * 生成验证码
     */
    @Operation(summary = "验证码")
    @RateLimit(key = RateLimitKey.LOGIN_CAPTCHA_KEY, time = 10, maxCount = 10, cacheType = CacheType.REDIS,
        limitType = RateLimit.LimitType.IP)
    @GetMapping("/captchaImage")
    public BaseResponseData<CaptchaDTO> getCaptchaImg() {
        CaptchaDTO captchaImg = loginService.generateCaptchaImg();
        return BaseResponseData.ok(captchaImg);
    }

    /**
     * 登录方法
     *
     * @param loginCommand 登录信息
     * @return 结果
     */
    @Operation(summary = "登录")
    @PostMapping("/login")
    public BaseResponseData<TokenDTO> login(@RequestBody LoginCommand loginCommand) {
        // 生成令牌
        String token = loginService.login(loginCommand);
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        CurrentLoginUserDTO currentUserDTO = userApplicationService.getLoginUserInfo(loginUser);
        return BaseResponseData.ok(new TokenDTO(token, currentUserDTO));
    }
    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/getLoginUserInfo")
    public BaseResponseData<CurrentLoginUserDTO> getLoginUserInfo() {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();

        CurrentLoginUserDTO currentUserDTO = userApplicationService.getLoginUserInfo(loginUser);

        return BaseResponseData.ok(currentUserDTO);
    }

    /**
     * 获取路由信息
     * TODO 如果要在前端开启路由缓存的话 需要在ServerConfig.json 中  设置CachingAsyncRoutes=true  避免一直重复请求路由接口
     * @return 路由信息
     */
    @Operation(summary = "获取用户对应的菜单路由", description = "用于动态生成路由")
    @GetMapping("/getRouters")
    public BaseResponseData<List<RouterDTO>> getRouters() {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        List<RouterDTO> routerTree = menuApplicationService.getRouterTree(loginUser);
        return BaseResponseData.ok(routerTree);
    }


    @Operation(summary = "注册接口", description = "暂未实现")
    @PostMapping("/register")
    public BaseResponseData<Void> register(@RequestBody AddUserCommand command) {
        return BaseResponseData.fail(new ApiException(ErrorCode.Business.COMMON_UNSUPPORTED_OPERATION));
    }

}
