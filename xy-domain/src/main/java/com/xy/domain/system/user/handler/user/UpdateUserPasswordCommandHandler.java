package com.xy.domain.system.user.handler.user;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.SysUserService;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserProfileModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.user.UpdateUserPasswordCommand;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserPasswordCommandHandler implements CommandHandler<UpdateUserPasswordCommand> {
   @Resource

   private SysUserService sysUserService;
    @Resource
    UserRepository userRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, UpdateUserPasswordCommand command) {
        UserModel model = userRepository.findByIdOrError(command.getUserId());
        UserProfileModel userProfileModel = new UserProfileModel();

        if(model.getUserId() == null){
            return userProfileModel.handle(eventQueue, command);
        }
        BeanUtils.copyProperties(model, userProfileModel);
        String oldPassword = userRepository.getPasswordByUserId(command.getUserId());
        Boolean matches = sysUserService.matchesPassword(command.getOldPassword(), oldPassword);
        userProfileModel.setNewOldPasswordIsEqual(matches);
        userProfileModel.setPassword(command.getNewPassword());
        Boolean handle = userProfileModel.handle(eventQueue, command);
        if(handle){
            userRepository.save(userProfileModel);
        }
        return false;


    }
}
