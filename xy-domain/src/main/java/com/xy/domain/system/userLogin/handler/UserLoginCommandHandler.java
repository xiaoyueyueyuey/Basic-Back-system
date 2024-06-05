package com.xy.domain.system.userLogin.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.userLogin.CaptchaService;
import com.xy.domain.system.userLogin.LoginService;
import com.xy.domain.system.userLogin.UserLoginModel;
import com.xy.domain.system.userLogin.command.UserLoginCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserLoginCommandHandler implements CommandHandler<UserLoginCommand> {
    @Resource
    private CaptchaService captchaService;
    @Resource
    private LoginService loginService;

    @Override
    public Boolean handle(EventQueue eventQueue, UserLoginCommand command) {
        UserLoginModel userLoginModel = new UserLoginModel();
        captchaService.validateCaptcha(command.getUsername(), command.getCaptchaCode(), command.getCaptchaCodeKey());
        loginService.userAuthenticate(command.getUsername(), command.getPassword());
        userLoginModel.handle(eventQueue, command);
        return true;
    }
}
