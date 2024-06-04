package com.xy.domain.system.role.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

import java.util.List;

@Data
public class RoleDataScopeUpdateEvent implements DomainEvent {
    private Long roleId;
    private List<Long> deptIds;
    private Integer dataScope;
}
