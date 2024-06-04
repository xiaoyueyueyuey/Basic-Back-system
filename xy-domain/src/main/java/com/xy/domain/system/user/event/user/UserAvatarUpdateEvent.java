package com.xy.domain.system.user.event.user;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class UserAvatarUpdateEvent implements DomainEvent {
    private Long userId;
    private String avatar;
}
