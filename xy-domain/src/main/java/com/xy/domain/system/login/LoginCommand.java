package com.xy.domain.system.login;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author valarchie
 */
@Data
public class LoginCommand {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String captchaCode;
    /**
     * 唯一标识
     */
    private String captchaCodeKey;

}
