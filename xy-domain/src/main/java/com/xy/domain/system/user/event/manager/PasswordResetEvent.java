package com.xy.domain.system.user.event.manager;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class PasswordResetEvent implements DomainEvent {
    private Long userId;
    private String password;
}
