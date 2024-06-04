package com.xy.domain.system.role.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class RoleDeleteEvent implements DomainEvent {
    private Long roleId;
}
