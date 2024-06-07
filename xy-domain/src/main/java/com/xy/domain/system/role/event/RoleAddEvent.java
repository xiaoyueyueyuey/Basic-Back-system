package com.xy.domain.system.role.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

import java.util.List;

@Data
public class RoleAddEvent implements DomainEvent {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String remark;
    private String dataScope;
    private String status;
    private List<Long> menuIds;
    public void setAggregateId(Long aggregateId){
        this.roleId = aggregateId;
    };


}
