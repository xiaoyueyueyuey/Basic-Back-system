package com.xy.domain.system.dept.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class DeptDeleteEvent implements DomainEvent {
    private Long deptId;
}
