package com.xy.domain.system.role.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class RoleStatusUpdateEvent implements DomainEvent {
    private Long roleId;
    private Integer status;
}
