package com.xy.domain.system.user.handler.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.manager.UpdateUserCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand> {

    @Resource
    private UserRepository userRepository;

    @Override
    public Boolean handle(EventQueue eventQueue, UpdateUserCommand command) {

        UserModel userModel = userRepository.findByIdOrError(command.getUserId());
        if (userModel.getUserId() == null) {
            //用户不存在，直接执行即可，聚合会抛出异常
            userModel.handle(eventQueue, command);
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
        //部门是否唯一
        if (ObjectUtil.isNotNull(command.getDeptId())) {
            if (userRepository.checkDeptIsExist(command.getDeptId())) {
                userModel.setDeptIsExist(true);
            } else {
                userModel.setDeptIsExist(false);
            }
        } else {
            userModel.setDeptIsExist(true);
        }
        //角色是否唯一
        if (ObjectUtil.isNotNull(command.getRoleId())) {
            if (userRepository.checkRoleIsExist(command.getRoleId())) {
                userModel.setRoleIsExist(true);
            } else {
                userModel.setRoleIsExist(false);
            }
        } else {
            userModel.setRoleIsExist(true);
        }
        //职位是否唯一
        if (ObjectUtil.isNotNull(command.getPostId())) {
            if (userRepository.checkPostIsExist(command.getPostId())) {
                userModel.setPostIsExist(true);
            } else {
                userModel.setPostIsExist(false);
            }
        } else {
            userModel.setPostIsExist(true);
        }
        userModel.setUserId(command.getUserId());
        Boolean handle = userModel.handle(eventQueue, command);
        if (handle) {
            return userRepository.save(userModel);
        }
        return false;

    }
}
