package com.xy.domain.system.user.event.user;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class UserPasswordUpdateEvent implements DomainEvent {
    private Long userId;
    private String password;
}
