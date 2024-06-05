package com.xy.domain.system.user.handler.manager;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.manager.DeleteUserCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand> {
    @Resource
    private UserRepository userRepository;


    @Override
    public Boolean handle(EventQueue eventQueue, DeleteUserCommand command) {
        UserModel userModel = new UserModel();
        Boolean handle = userModel.handle(eventQueue, command);
        if (handle) {
            return userRepository.deleteBatchByIds(Collections.singletonList(userModel.getUserId()));
        }
        return false;
    }
}
