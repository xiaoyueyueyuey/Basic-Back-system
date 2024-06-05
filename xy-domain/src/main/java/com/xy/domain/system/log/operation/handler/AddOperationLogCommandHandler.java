package com.xy.domain.system.log.operation.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.log.operation.OperationLogModel;
import com.xy.domain.system.log.operation.command.AddOperationLogCommand;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AddOperationLogCommandHandler implements CommandHandler<AddOperationLogCommand> {

    @Override
    public Boolean handle(EventQueue eventQueue, AddOperationLogCommand command) {
        OperationLogModel operationLogModel = new OperationLogModel();
        Boolean handle = operationLogModel.handle(eventQueue, command);
        return handle;
    }
}
