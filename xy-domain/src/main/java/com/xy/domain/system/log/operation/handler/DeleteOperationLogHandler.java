package com.xy.domain.system.log.operation.handler;


import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;

import com.xy.domain.system.log.operation.OperationLogModel;
import com.xy.domain.system.log.operation.command.DeleteOperationLogCommand;
import org.springframework.stereotype.Component;

@Component
public class DeleteOperationLogHandler implements CommandHandler<DeleteOperationLogCommand> {

    @Override
    public Boolean handle(EventQueue eventQueue, DeleteOperationLogCommand command) {
        OperationLogModel operationLogModel = new OperationLogModel();
        operationLogModel.handle(eventQueue, command);
        return true;
    }

}
