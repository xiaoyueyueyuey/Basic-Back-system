package com.xy.domain.system.log.operation.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

import java.util.List;

@Data
public class OperationLogDeleteEvent implements DomainEvent {
        private List<Long> operationIds;
}
