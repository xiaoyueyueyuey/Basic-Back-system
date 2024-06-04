package com.xy.admin.query;

import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.mapper.SysUserMapper;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.user.event.user.UserAvatarUpdateEvent;
import com.xy.domain.system.user.event.user.UserPasswordUpdateEvent;
import com.xy.domain.system.user.event.user.UserProfileUpdateEvent;
import com.xy.infrastructure.user.AuthenticationUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SysUserProfileMaterialize implements DomainEventListener {
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public void onEvent(DomainEvent event) {
        if(event instanceof UserProfileUpdateEvent){
            updateSysUserProfile((UserProfileUpdateEvent) event);
        }else if(event instanceof UserAvatarUpdateEvent){
            updateSysUserAvatar((UserAvatarUpdateEvent) event);
        } else if (event instanceof UserPasswordUpdateEvent) {
            updateSysUserPassword((UserPasswordUpdateEvent) event);
        }

    }

    private void updateSysUserPassword(UserPasswordUpdateEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        AuthenticationUtils.encryptPassword(event.getPassword());
        sysUserEntity.setPassword(event.getPassword());
        sysUserEntity.setUserId(event.getUserId());
        sysUserMapper.updateById(sysUserEntity);
    }

    private void updateSysUserAvatar(UserAvatarUpdateEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setAvatar(event.getAvatar());
        sysUserEntity.setUserId(event.getUserId());
        sysUserMapper.updateById(sysUserEntity);
    }

    private void updateSysUserProfile(UserProfileUpdateEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        BeanUtils.copyProperties(event, sysUserEntity);
        sysUserMapper.updateById(sysUserEntity);
    }
}
