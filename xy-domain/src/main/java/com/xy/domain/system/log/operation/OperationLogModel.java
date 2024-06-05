package com.xy.domain.system.log.operation;

import com.xy.domain.EventQueue;
import com.xy.domain.system.log.operation.command.AddOperationLogCommand;
import com.xy.domain.system.log.operation.command.DeleteOperationLogCommand;
import com.xy.domain.system.log.operation.event.OperationLogAddEvent;
import com.xy.domain.system.log.operation.event.OperationLogDeleteEvent;
import org.springframework.beans.BeanUtils;

public class OperationLogModel {
    public Boolean handle(EventQueue eventQueue, DeleteOperationLogCommand command) {
        OperationLogDeleteEvent operationLogDeleteEvent = new OperationLogDeleteEvent();
        operationLogDeleteEvent.setOperationIds(command.getOperationIds());
        eventQueue.enqueue(operationLogDeleteEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, AddOperationLogCommand command) {
        OperationLogAddEvent operationLogAddEvent = new OperationLogAddEvent();
        BeanUtils.copyProperties(command, operationLogAddEvent);
        eventQueue.enqueue(operationLogAddEvent);
        return true;
    }
}
