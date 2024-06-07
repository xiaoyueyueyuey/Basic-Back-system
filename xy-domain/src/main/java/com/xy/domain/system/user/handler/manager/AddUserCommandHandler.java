package com.xy.domain.system.user.handler.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserRepository;
import com.xy.domain.system.user.command.manager.AddUserCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AddUserCommandHandler implements CommandHandler<AddUserCommand> {
    @Resource
    private UserRepository userRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, AddUserCommand command) {
        UserModel userModel = new UserModel();
        //名字是否唯一
        if(userRepository.checkUsernameIsUnique(command.getUsername())){
            userModel.setUsername(command.getUsername());
            userModel.setUserNameIsUnique(true);
        }else {
            userModel.setUsername(command.getUsername());
            userModel.setUserNameIsUnique(false);
        }
        //邮箱是否唯一
        if(!StrUtil.isEmptyIfStr(command.getEmail())){
            if(userRepository.checkEmailIsUnique(command.getEmail())){
                userModel.setEmail(command.getEmail());
                userModel.setEmailIsUnique(true);
            }else {
                userModel.setEmail(command.getEmail());
                userModel.setEmailIsUnique(false);
            }
        }else {
            userModel.setEmailIsUnique(true);
        }
//        //电话号码是否唯一
        if(!StrUtil.isEmptyIfStr(command.getPhoneNumber())){
            if(userRepository.checkPhoneNumberIsUnique(command.getPhoneNumber())){
                userModel.setPhoneNumber(command.getPhoneNumber());
                userModel.setPhoneNumberIsUnique(true);
            }else {
                userModel.setPhoneNumber(command.getPhoneNumber());
                userModel.setPhoneNumberIsUnique(false);
            }
        }else {
            userModel.setPhoneNumberIsUnique(true);
        }
        //部门是否唯一
        if(ObjectUtil.isNotNull(command.getDeptId())){
            if(userRepository.checkDeptIsExist(command.getDeptId())){
                userModel.setDeptIsExist(true);
            }else {
                userModel.setDeptIsExist(false);
            }
        }else {
            userModel.setDeptIsExist(true);
        }
        //角色是否唯一
        if(ObjectUtil.isNotNull(command.getRoleId())){
            if(userRepository.checkRoleIsExist(command.getRoleId())){
                userModel.setRoleIsExist(true);
            }else {
                userModel.setRoleIsExist(false);
            }
        }else {
            userModel.setRoleIsExist(true);
        }
        //职位是否唯一
        if(ObjectUtil.isNotNull(command.getPostId())){
            if(userRepository.checkPostIsExist(command.getPostId())){
                userModel.setPostIsExist(true);
            }else {
                userModel.setPostIsExist(false);
            }
        }else {
            userModel.setPostIsExist(true);
        }
        Boolean handle = userModel.handle(eventQueue, command);
        if (handle) {
            Long userId = userRepository.save(userModel);
            eventQueue.queue().forEach(event -> event.setAggregateId(userId));
            return true;
        }
        return false;

    }
}
