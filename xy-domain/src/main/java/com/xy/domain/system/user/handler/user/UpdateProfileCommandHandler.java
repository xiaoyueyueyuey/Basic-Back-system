package com.xy.domain.system.user.handler.user;

import cn.hutool.core.util.StrUtil;
import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserProfileModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.user.UpdateUserProfileCommand;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
@Component
public class UpdateProfileCommandHandler implements CommandHandler<UpdateUserProfileCommand> {
    @Resource
    UserRepository userRepository;

    @Override
    public Boolean handle(EventQueue eventQueue, UpdateUserProfileCommand command) {
        UserModel model = userRepository.findByIdOrError(command.getUserId());
        UserProfileModel userModel = new UserProfileModel();
        BeanUtils.copyProperties(model, userModel);

        if (model.getUserId() == null) {
           return userModel.handle(eventQueue,command);
        }

        //名字是否唯一,如果名字没有变化，不需要判断是否唯一
        if ((userModel.getUsername().equals(command.getUsername()))||userRepository.checkUsernameIsUnique(command.getUsername(), command.getUserId())) {
            userModel.setUsername(command.getUsername());
            userModel.setUserNameIsUnique(true);
        } else {
            userModel.setUsername(command.getUsername());
            userModel.setUserNameIsUnique(false);
        }
        //邮箱是否唯一,如果邮箱没有变化，不需要判断是否唯一
        if (!StrUtil.isEmptyIfStr(command.getEmail())) {
            if (userModel.getEmail()!=null&&(userModel.getEmail().equals(command.getEmail())||userRepository.checkEmailIsUnique(command.getEmail(),command.getUserId()))) {
                userModel.setEmail(command.getEmail());
                userModel.setEmailIsUnique(true);
            } else {
                userModel.setEmail(command.getEmail());
                userModel.setEmailIsUnique(false);
            }
        } else {
            userModel.setEmailIsUnique(true);
        }
//        //电话号码是否唯一
        if (!StrUtil.isEmptyIfStr(command.getPhoneNumber())) {
            if (userModel.getPhoneNumber()!=null && ( userModel.getPhoneNumber().equals(command.getPhoneNumber())||userRepository.checkPhoneNumberIsUnique(command.getPhoneNumber(),command.getUserId()))) {
                userModel.setPhoneNumber(command.getPhoneNumber());
                userModel.setPhoneNumberIsUnique(true);
            } else {
                userModel.setPhoneNumber(command.getPhoneNumber());
                userModel.setPhoneNumberIsUnique(false);
            }
        } else {
            userModel.setPhoneNumberIsUnique(true);
        }


        Boolean handle = userModel.handle(eventQueue, command);
        if (handle) {
            UserModel userModel1 = new UserModel();
            BeanUtils.copyProperties(userModel, userModel1);
            userRepository.save(userModel1);
            return true;
        }

        return false;
    }
}
