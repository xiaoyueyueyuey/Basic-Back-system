package com.xy.domain.system.userLogin.command;

import com.xy.domain.system.Command;
import lombok.Data;

@Data
public class UserLoginCommand implements Command {
    private String username;
    private String password;
    private String captchaCode;
    private String captchaCodeKey;
}
