package com.xy.domain.system.notice.event;

import com.xy.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NoticeDeleteEvent implements DomainEvent {
    private Long noticeId;
}
