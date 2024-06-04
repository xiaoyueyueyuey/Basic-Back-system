package com.xy.domain.system.role.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.role.RoleModel;
import com.xy.domain.system.role.RoleRepository;
import com.xy.domain.system.role.command.UpdateRoleStatusCommand;
import jakarta.annotation.Resource;

public class UpdateRoleStatusCommandHandler implements CommandHandler<UpdateRoleStatusCommand> {
    @Resource
    private RoleRepository roleRepository;

    @Override
    public Boolean handle(EventQueue eventQueue, UpdateRoleStatusCommand command) {
        RoleModel model = roleRepository.findByIdOrError(command.getRoleId());
        model.setStatus(command.getStatus());
        Boolean handle = model.handle(eventQueue, command);
        if(handle){
            return roleRepository.save(model);
        }
        return false;
    }
}
