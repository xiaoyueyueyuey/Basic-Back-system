package com.xy.domain.system.role.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.role.RoleModel;
import com.xy.domain.system.role.RoleRepository;
import com.xy.domain.system.role.command.AddRoleCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AddRoleCommandHandler implements CommandHandler<AddRoleCommand> {

    @Resource
    private RoleRepository roleRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, AddRoleCommand command) {
        Boolean roleKeyIsUnique = roleRepository.checkRoleKeyIsUnique(command.getRoleKey());
        Boolean roleNameIsUnique = roleRepository.checkRoleNameIsUnique(command.getRoleName());
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleNameIsUnique(roleNameIsUnique);
        roleModel.setRoleKeyIsUnique(roleKeyIsUnique);
        roleModel.setRoleIsAssignToUserCount(0L);
        roleModel.setRoleKey(command.getRoleKey());
        roleModel.setRoleName(command.getRoleName());
        roleModel.setStatus(command.getStatus());
        Boolean handle = roleModel.handle(eventQueue, command);
        if(handle){
           return roleRepository.save(roleModel);
        }
        return false;
    }
}
