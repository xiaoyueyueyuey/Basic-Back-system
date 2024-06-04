package com.xy.domain.system.user.handler.manager;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.manager.ChangeStatusCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ChangeStatusCommandHandler implements CommandHandler<ChangeStatusCommand>{

    @Resource
    private UserRepository userRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, ChangeStatusCommand command) {

        UserModel byIdOrError = userRepository.findByIdOrError(command.getUserId());
        Boolean handle = byIdOrError.handle(eventQueue, command);
        if (handle) {
            return userRepository.save(byIdOrError);
        }
        return false;

    }
}
