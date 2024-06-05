package com.xy.admin.controller;

import com.xy.admin.common.dto.common.CurrentLoginUserDTO;
import com.xy.admin.common.dto.common.TokenDTO;
import com.xy.admin.customize.securityService.TokenService;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.admin.dto.menu.RouterDTO;
import com.xy.admin.query.service.SysMenuService;
import com.xy.admin.query.service.SysUserService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.exception.ApiException;
import com.xy.domain.system.user.command.manager.AddUserCommand;
import com.xy.domain.system.user.handler.manager.AddUserCommandHandler;
import com.xy.domain.system.userLogin.command.UserLoginCommand;
import com.xy.domain.system.userLogin.handler.UserLoginCommandHandler;
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
    private final SysUserService sysUserService;
    private final SysMenuService menuService;
    private final CommandInvoker commandInvoker;
    private  final AddUserCommandHandler addUserCommandHandler;
    private final TokenService tokenService;
    private final UserLoginCommandHandler  loginCommandHandler;
    /**
     * 登录方法
     *
     * @param loginCommand 登录信息
     * @return 结果
     */
    @Operation(summary = "登录")
    @PostMapping("/login")
    public BaseResponseData<TokenDTO> login(@RequestBody UserLoginCommand loginCommand) {
//        String token = loginServiceImpl.login(loginCommand);// 生成token
        try {
            commandInvoker.execute(loginCommandHandler, loginCommand);
        }catch (ApiException e){
            return BaseResponseData.fail(e);
        }
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();// 从上下文中获取当前登录用户信息
        String token = tokenService.createTokenAndPutUserInCache(loginUser);// 生成token并将用户信息放入缓存
        CurrentLoginUserDTO currentUserDTO = sysUserService.getLoginUserInfo(loginUser);// 获取当前登录用户信息，包括权限，角色等
        //currentUserDTO的结构是  UserDTO 用户基本信息 | String roleKey | Set<String> permissions;
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
        CurrentLoginUserDTO currentUserDTO = sysUserService.getLoginUserInfo(loginUser);
        return BaseResponseData.ok(currentUserDTO);
    }

    /**
     * 获取路由信息
     * TODO 如果要在前端开启路由缓存的话 需要在ServerConfig.json 中  设置CachingAsyncRoutes=true  避免一直重复请求路由接口
     *
     * @return 路由信息
     */
    @Operation(summary = "获取用户对应的菜单路由", description = "用于动态生成路由")
    @GetMapping("/getRouters")
    public BaseResponseData<List<RouterDTO>> getRouters() {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        List<RouterDTO> routerTree = menuService.getRouterTree(loginUser);
        return BaseResponseData.ok(routerTree);
    }
    @Operation(summary = "注册接口", description = "暂未实现")
    @PostMapping("/register")
    public BaseResponseData<Void> register(@RequestBody AddUserCommand command) {
        try {
            commandInvoker.execute(addUserCommandHandler, command);
        } catch (ApiException e) {
            return BaseResponseData.fail(e);
        }
        return BaseResponseData.ok();
    }

}
