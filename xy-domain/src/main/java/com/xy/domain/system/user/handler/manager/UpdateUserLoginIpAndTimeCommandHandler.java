package com.xy.domain.system.user.handler.manager;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.command.manager.UpdateUserLoginIpAndTimeCommand;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserLoginIpAndTimeCommandHandler implements CommandHandler<UpdateUserLoginIpAndTimeCommand> {
    @Override
    public Boolean handle(EventQueue eventQueue, UpdateUserLoginIpAndTimeCommand command) {

        UserModel userModel = new UserModel();
       return userModel.handle(eventQueue, command);
    }
}
