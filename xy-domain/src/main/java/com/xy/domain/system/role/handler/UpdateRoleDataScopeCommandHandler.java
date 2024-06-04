package com.xy.domain.system.role.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.role.RoleModel;
import com.xy.domain.system.role.RoleRepository;
import com.xy.domain.system.role.command.UpdateRoleDataScopeCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UpdateRoleDataScopeCommandHandler implements CommandHandler<UpdateRoleDataScopeCommand> {
    @Resource
    private RoleRepository roleRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, UpdateRoleDataScopeCommand command) {
        RoleModel model = roleRepository.findByIdOrError(command.getRoleId());
        if(model.getRoleId()==null){
            return model.handle(eventQueue, command);
        }
        //这个聚合不存数据范围，事件才存
        return model.handle(eventQueue, command);
    }
}
