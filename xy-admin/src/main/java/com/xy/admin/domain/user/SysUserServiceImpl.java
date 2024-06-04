package com.xy.admin.domain.user;

import com.xy.domain.system.user.SysUserService;
import com.xy.infrastructure.user.AuthenticationUtils;
import org.springframework.stereotype.Component;

@Component
public class SysUserServiceImpl implements SysUserService {
    @Override
    public Boolean matchesPassword(String rawPassword, String encodedPassword) {

        return AuthenticationUtils.matchesPassword(rawPassword, encodedPassword);

    }
}
