package com.xy.admin.query;

import com.xy.admin.entity.SysLoginInfoEntity;
import com.xy.admin.mapper.SysLoginInfoMapper;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.log.login.event.LoginLogAddEvent;
import com.xy.domain.system.log.login.event.LoginLogDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor

@Component
public class SysLoginInfoMaterialize implements DomainEventListener {
   final SysLoginInfoMapper sysLoginInfoMapper;

    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof LoginLogDeleteEvent) {
            deleteLoginInfo((LoginLogDeleteEvent) event);
        } else if (event instanceof LoginLogAddEvent) {
            addLoginInfo((LoginLogAddEvent) event);

        }
    }

    private void addLoginInfo(LoginLogAddEvent event) {
        SysLoginInfoEntity sysLoginInfoEntity = new SysLoginInfoEntity();
        BeanUtils.copyProperties(event, sysLoginInfoEntity);
        sysLoginInfoMapper.insert(sysLoginInfoEntity);
    }

    private void deleteLoginInfo(LoginLogDeleteEvent event) {
        sysLoginInfoMapper.deleteBatchIds(event.getInfoIds());
    }
}
