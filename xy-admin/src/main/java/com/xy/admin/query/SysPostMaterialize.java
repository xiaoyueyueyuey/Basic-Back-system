package com.xy.admin.query;

import com.xy.admin.entity.SysPostEntity;
import com.xy.admin.mapper.SysPostMapper;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.post.event.PostAddEvent;
import com.xy.domain.system.post.event.PostDeleteEvent;
import com.xy.domain.system.post.event.PostUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor

@Component
public class SysPostMaterialize implements DomainEventListener {
    final SysPostMapper sysPostMapper;

    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof PostAddEvent) addSysPost((PostAddEvent) event);
        else if (event instanceof PostUpdateEvent) updateSysPost((PostUpdateEvent) event);
        else if (event instanceof PostDeleteEvent) deleteSysPost((PostDeleteEvent) event);
    }

    private void deleteSysPost(PostDeleteEvent event) {
        sysPostMapper.deleteById(event.getPostId());
    }

    private void updateSysPost(PostUpdateEvent event) {
        SysPostEntity sysPostEntity = new SysPostEntity();
        BeanUtils.copyProperties(event, sysPostEntity);
        sysPostMapper.updateById(sysPostEntity);
    }

    private void addSysPost(PostAddEvent event) {
        SysPostEntity sysPostEntity = new SysPostEntity();
        BeanUtils.copyProperties(event, sysPostEntity);
        sysPostMapper.insert(sysPostEntity);

    }
}
