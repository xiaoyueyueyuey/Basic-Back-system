package com.xy.domain.system.role.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.role.RoleModel;
import com.xy.domain.system.role.RoleRepository;
import com.xy.domain.system.role.command.UpdateRoleCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UpdateRoleCommandHandler implements CommandHandler<UpdateRoleCommand> {
    @Resource
    private RoleRepository roleRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, UpdateRoleCommand command) {
        RoleModel model = roleRepository.findByIdOrError(command.getRoleId());
        if(model.getRoleId()==null){
            return model.handle(eventQueue, command);
        }

        if(!model.getRoleKey().equals(command.getRoleKey())){
//            不同，需要判断是否重复
            Boolean roleKeyIsUnique = roleRepository.checkRoleKeyIsUnique(command.getRoleKey(),model.getRoleId());
            if(!roleKeyIsUnique){
                model.setRoleKeyIsUnique(false);
            }else {
                model.setRoleKeyIsUnique(true);
                model.setRoleKey(command.getRoleKey());
            }
        }else {
            model.setRoleKeyIsUnique(true);
        }
        if(!model.getRoleName().equals(command.getRoleName())){
//            不同，需要判断是否重复
            Boolean roleNameIsUnique = roleRepository.checkRoleNameIsUnique(command.getRoleName(),model.getRoleId());
            if(!roleNameIsUnique){
                model.setRoleNameIsUnique(false);
            }else {
                model.setRoleNameIsUnique(true);
                model.setRoleName(command.getRoleName());
            }
        }else {
            model.setRoleNameIsUnique(true);
        }
        model.setStatus(command.getStatus());
        Boolean handle = model.handle(eventQueue, command);
        if(handle){
            return roleRepository.save(model);
        }
        return false;
    }
}
