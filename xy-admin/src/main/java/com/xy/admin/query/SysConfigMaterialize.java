package com.xy.admin.query;

import com.xy.admin.entity.SysConfigEntity;
import com.xy.admin.mapper.SysConfigMapper;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.config.ConfigUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class SysConfigMaterialize implements DomainEventListener {
    private final SysConfigMapper sysConfigMapper;
    @Override
    public void onEvent(DomainEvent event) {
        if(event instanceof ConfigUpdateEvent){
            updateConfig((ConfigUpdateEvent) event);
        }
    }
    private void updateConfig(ConfigUpdateEvent event) {
        SysConfigEntity sysConfigEntity = new SysConfigEntity();
        sysConfigEntity.setConfigId(event.getConfigId());
        sysConfigEntity.setConfigValue(event.getConfigValue());
        sysConfigMapper.updateById(sysConfigEntity);
    }
}
