package com.xy.domain.system.user.event.manager;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class UserDeleteEvent implements DomainEvent {
    private Long userId;
}
