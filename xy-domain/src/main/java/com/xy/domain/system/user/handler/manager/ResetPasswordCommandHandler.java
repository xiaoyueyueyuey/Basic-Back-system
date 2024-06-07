package com.xy.domain.system.user.handler.manager;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.manager.ResetPasswordCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordCommandHandler implements CommandHandler<ResetPasswordCommand> {
    @Resource
    private UserRepository userRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, ResetPasswordCommand command) {
        UserModel userModel = userRepository.findByIdOrError(command.getUserId());
        Boolean handle = userModel.handle(eventQueue, command);
        if (handle) {
            return userRepository.save(userModel)>0;
        }
        return false;
    }
}
