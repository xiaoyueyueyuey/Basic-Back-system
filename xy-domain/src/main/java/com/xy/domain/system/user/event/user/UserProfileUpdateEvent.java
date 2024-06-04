package com.xy.domain.system.user.event.user;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class UserProfileUpdateEvent implements DomainEvent {
    private Long userId;
    private Integer sex;
    private String username;
    private String nickName;
    private String phoneNumber;
    private String email;
}
