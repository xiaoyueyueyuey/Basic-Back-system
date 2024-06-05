package com.xy.admin.query;

import com.xy.admin.entity.SysNoticeEntity;
import com.xy.admin.mapper.SysNoticeMapper;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.notice.event.NoticeAddEvent;
import com.xy.domain.system.notice.event.NoticeDeleteEvent;
import com.xy.domain.system.notice.event.NoticeUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor

@Component
public class SysNoticeMaterialize implements DomainEventListener {
    private final SysNoticeMapper sysNoticeMapper;
    @Override
    public void onEvent(DomainEvent event) {
        if(event instanceof NoticeAddEvent) addSysNotice((NoticeAddEvent) event);
        else if(event instanceof NoticeUpdateEvent) updateSysNotice((NoticeUpdateEvent) event);
        else if(event instanceof NoticeDeleteEvent) deleteSysNotice((NoticeDeleteEvent) event);
    }

    private void deleteSysNotice(NoticeDeleteEvent event) {
        sysNoticeMapper.deleteById(event.getNoticeId());
    }

    private void updateSysNotice(NoticeUpdateEvent event) {
        SysNoticeEntity sysNoticeEntity = new SysNoticeEntity();
        BeanUtils.copyProperties(event, sysNoticeEntity);
        sysNoticeMapper.updateById(sysNoticeEntity);
    }

    private void addSysNotice(NoticeAddEvent event) {
        SysNoticeEntity sysNoticeEntity = new SysNoticeEntity();
        BeanUtils.copyProperties(event, sysNoticeEntity);
        sysNoticeMapper.insert(sysNoticeEntity);
    }
}
