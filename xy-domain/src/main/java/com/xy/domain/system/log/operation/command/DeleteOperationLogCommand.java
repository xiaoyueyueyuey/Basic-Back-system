package com.xy.domain.system.log.operation.command;

import com.xy.domain.system.Command;
import lombok.Data;

import java.util.List;

@Data
public class DeleteOperationLogCommand implements Command {
    private List<Long> operationIds;
}
