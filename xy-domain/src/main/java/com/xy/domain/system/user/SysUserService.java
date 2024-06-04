package com.xy.domain.system.user;

public interface SysUserService {

    //解密码
    public Boolean matchesPassword(String rawPassword, String encodedPassword);
}
