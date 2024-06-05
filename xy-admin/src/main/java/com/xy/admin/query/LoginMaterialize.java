package com.xy.admin.query;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xy.admin.customize.async.AsyncTaskFactory;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.common.enums.common.LoginStatusEnum;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.user.command.manager.UpdateUserLoginIpAndTimeCommand;
import com.xy.domain.system.user.handler.manager.UpdateUserLoginIpAndTimeCommandHandler;
import com.xy.domain.system.userLogin.event.UserLoginEvent;
import com.xy.infrastructure.thread.ThreadPoolManager;
import com.xy.infrastructure.user.AuthenticationUtils;
import com.xy.infrastructure.user.web.SystemLoginUser;
import com.xy.infrastructure.utils.ServletHolderUtil;
import org.springframework.stereotype.Component;

@Component
public class LoginMaterialize implements DomainEventListener {
    @Override
    public void onEvent(DomainEvent event) {
        if(event instanceof UserLoginEvent){
            //添加用户登录日志
            addLoginLog();
        }
    }
    private void addLoginLog() {
        //从上下文中获取用户信息
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        recordLoginInfo(loginUser);//记录登录信息,并且更新用户登录时间和登录ip
    }
    /**
     * 记录登录信息
     *
     * @param loginUser 登录用户
     */
    public void recordLoginInfo(SystemLoginUser loginUser) {
        ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginUser.getUsername(), LoginStatusEnum.LOGIN_SUCCESS,
                LoginStatusEnum.LOGIN_SUCCESS.description()));// 记录登录日志
        //感觉这里应该改一下，不应该从缓存中获取用户信息，因为我只需要改变登录时间和登录ip
//        SysUserEntity entity = redisCache.userCache.getObjectById(loginUser.getUserId());// 从缓存中获取用户信息
        UpdateUserLoginIpAndTimeCommand command = new UpdateUserLoginIpAndTimeCommand();
        command.setUserId(loginUser.getUserId());
        command.setLoginIp(ServletUtil.getClientIP(ServletHolderUtil.getRequest()));// 设置登录ip
        command.setLoginDate(DateUtil.date());// 设置登录时间
        UpdateUserLoginIpAndTimeCommandHandler handler = SpringUtil.getBean(UpdateUserLoginIpAndTimeCommandHandler.class);
        CommandInvoker invoker = SpringUtil.getBean(CommandInvoker.class);
        invoker.execute(handler, command);
    }
}
