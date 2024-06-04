package com.xy.domain.system.user.event.manager;

import com.xy.domain.DomainEvent;

public class StatusChangeEvent implements DomainEvent {
    private Long userId;
    private Integer status;
}
