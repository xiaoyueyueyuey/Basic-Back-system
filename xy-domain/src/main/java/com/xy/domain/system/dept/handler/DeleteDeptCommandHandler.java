package com.xy.domain.system.dept.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.dept.DeptModel;
import com.xy.domain.system.dept.DeptRepository;
import com.xy.domain.system.dept.command.DeleteDeptCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component

public class DeleteDeptCommandHandler implements CommandHandler<DeleteDeptCommand> {
    @Resource
    private DeptRepository deptRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, DeleteDeptCommand command) {
        DeptModel deptModel = deptRepository.findByIdOrError(command.getDeptId());

        Boolean handle = deptModel.handle(eventQueue, command);
        if(handle) {
           return deptRepository.deleteById(deptModel.getDeptId());
        }
        return false;

    }
}
