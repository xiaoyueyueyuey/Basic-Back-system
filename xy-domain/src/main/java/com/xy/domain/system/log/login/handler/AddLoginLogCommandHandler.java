package com.xy.domain.system.log.login.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.log.login.LoginLogModel;
import com.xy.domain.system.log.login.command.AddLoginLogCommand;
import org.springframework.stereotype.Component;

@Component
public class AddLoginLogCommandHandler implements CommandHandler<AddLoginLogCommand> {

    @Override
    public Boolean handle(EventQueue eventQueue, AddLoginLogCommand command) {
        LoginLogModel loginLogModel = new LoginLogModel();
        loginLogModel.handle(eventQueue, command);
        return true;
    }
}
