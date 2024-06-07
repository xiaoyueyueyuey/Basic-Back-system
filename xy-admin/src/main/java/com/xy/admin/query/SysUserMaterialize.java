package com.xy.admin.query;


import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.mapper.SysUserMapper;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.user.event.manager.*;
import com.xy.infrastructure.user.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor

@Component
public class SysUserMaterialize implements DomainEventListener {
    private final SysUserMapper mapper;
    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof UserAddEvent) {
            addSysUser((UserAddEvent) event);
        } else if (event instanceof UserUpdateEvent) {
            updateSysUser((UserUpdateEvent) event);
        } else if (event instanceof UserDeleteEvent) {
            deleteSysUser((UserDeleteEvent) event);
        } else if (event instanceof StatusChangeEvent) {
            changeStatus((StatusChangeEvent) event);
        } else if (event instanceof PasswordResetEvent) {
            resetPassword((PasswordResetEvent) event);
        } else if (event instanceof UserLoginIpAndTimeUpdateEvent) {
            updateUserLoginIpAndTime((UserLoginIpAndTimeUpdateEvent) event);

        }
    }
    private void updateUserLoginIpAndTime(UserLoginIpAndTimeUpdateEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        BeanUtils.copyProperties(event, sysUserEntity);
        mapper.updateById(sysUserEntity);
    }
    public void addSysUser(UserAddEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        BeanUtils.copyProperties(event, sysUserEntity);
        int insert = mapper.insert(sysUserEntity);
        if(insert >0){
            //TODO 新增成功，如果有部门，需要维护部门聚合，如果有角色，需要维护角色聚合，如果有岗位，需要维护岗位聚合
        }
    }

    public void updateSysUser(UserUpdateEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        BeanUtils.copyProperties(event, sysUserEntity);
        mapper.updateById(sysUserEntity);
    }
    public void deleteSysUser(UserDeleteEvent event) {
        mapper.deleteById(event.getUserId());
    }

    public void changeStatus(StatusChangeEvent event) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        BeanUtils.copyProperties(event, sysUserEntity);
        mapper.updateById(sysUserEntity);
    }

    public void resetPassword(PasswordResetEvent event) {
        //密码加密
        String s = AuthenticationUtils.encryptPassword(event.getPassword());
        event.setPassword(s);
        SysUserEntity sysUserEntity = new SysUserEntity();
        BeanUtils.copyProperties(event, sysUserEntity);
        mapper.updateById(sysUserEntity);
    }
}
