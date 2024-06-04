package com.xy.admin.query.service.application;

import com.xy.admin.common.cache.CacheCenter;
import com.xy.admin.common.dto.common.CurrentLoginUserDTO;
import com.xy.admin.dto.user.UserDTO;
import com.xy.admin.dto.user.UserProfileDTO;
import com.xy.admin.entity.SysPostEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.query.service.SysUserService;
import com.xy.domain.system.user.*;
import com.xy.domain.system.user.command.user.UpdateUserAvatarCommand;
import com.xy.domain.system.user.command.user.UpdateUserPasswordCommand;
import com.xy.domain.system.user.command.user.UpdateUserProfileCommand;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final SysUserService userService;

    /**
     * 获取用户详情
     * @param userId
     * @return
     */

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    public CurrentLoginUserDTO getLoginUserInfo(SystemLoginUser loginUser) {
        CurrentLoginUserDTO permissionDTO = new CurrentLoginUserDTO();
        permissionDTO.setUserInfo(new UserDTO(CacheCenter.userCache.getObjectById(loginUser.getUserId())));
        permissionDTO.setRoleKey(loginUser.getRoleInfo().getRoleKey());
        permissionDTO.setPermissions(loginUser.getRoleInfo().getMenuPermissions());
        return permissionDTO;
    }


}
