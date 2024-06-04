package com.xy.admin.query;

import com.xy.admin.entity.SysMenuEntity;
import com.xy.admin.mapper.SysMenuMapper;
import com.xy.common.utils.jackson.JacksonUtil;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.menu.event.MenuAddEvent;
import com.xy.domain.system.menu.event.MenuDeleteEvent;
import com.xy.domain.system.menu.event.MenuUpdateEvent;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SysMenuMaterialize implements DomainEventListener {
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public void onEvent(DomainEvent event) {
        if(event instanceof MenuAddEvent) addSysMenu((MenuAddEvent) event);
        else if(event instanceof MenuUpdateEvent) updateSysMenu((MenuUpdateEvent) event);
        else if(event instanceof MenuDeleteEvent) deleteSysMenu((MenuDeleteEvent) event);

    }
    private void addSysMenu(MenuAddEvent event) {
        //TODO 同步menuId
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        BeanUtils.copyProperties(event ,sysMenuEntity);
        String metaInfo= JacksonUtil.to(event.getMeta());
        sysMenuEntity.setMetaInfo(metaInfo);
        sysMenuMapper.insert(sysMenuEntity);

    }
    private void updateSysMenu(MenuUpdateEvent event) {
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        BeanUtils.copyProperties(event, sysMenuEntity);
        String metaInfo= JacksonUtil.to(event.getMeta());
        sysMenuEntity.setMetaInfo(metaInfo);
        sysMenuMapper.updateById(sysMenuEntity);
    }
    private void deleteSysMenu(MenuDeleteEvent event) {
        sysMenuMapper.deleteById(event.getMenuId());
    }
}
