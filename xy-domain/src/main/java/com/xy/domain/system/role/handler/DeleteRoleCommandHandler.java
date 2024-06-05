package com.xy.domain.system.role.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.role.RoleModel;
import com.xy.domain.system.role.RoleRepository;
import com.xy.domain.system.role.command.DeleteRoleCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DeleteRoleCommandHandler implements CommandHandler<DeleteRoleCommand> {
    @Resource
    private RoleRepository roleRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, DeleteRoleCommand command) {
        RoleModel model = roleRepository.findByIdOrError(command.getRoleId());
        Boolean handle = model.handle(eventQueue, command);
        if(handle){
            return roleRepository.deleteBatchByIds(Collections.singletonList(model.getRoleId()));
        }
        return false;
    }
}
