package com.xy.domain.system.user;

import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.command.manager.*;
import com.xy.domain.system.user.event.manager.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * 用户模型(管理端)
 */
@Data
@NoArgsConstructor
public class UserModel {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String email;
    //下面的属性不用存储到数据库
    private Boolean userNameIsUnique;
    private Boolean emailIsUnique;
    private Boolean phoneNumberIsUnique;
    private Boolean roleIsExist;
    private Boolean deptIsExist;
    private Boolean postIsExist;
    public Boolean handle(EventQueue eventQueue, AddUserCommand command) {
        try {
            checkUsernameIsUnique();
            checkPhoneNumberIsUnique();
            checkEmailIsUnique();
            checkFieldRelatedEntityExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new UserAddFailedEvent());
            return false;
        }
        UserAddEvent userAddEvent = new UserAddEvent();
        BeanUtils.copyProperties(command, userAddEvent);
        eventQueue.enqueue(userAddEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, UpdateUserCommand command) {
        try {
            checkUserIsExist();
            checkUsernameIsUnique();
            checkPhoneNumberIsUnique();
            checkEmailIsUnique();
            checkFieldRelatedEntityExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new UserUpdateFailedEvent());
            return false;
        }
        UserUpdateEvent userUpdateEvent = new UserUpdateEvent();
        BeanUtils.copyProperties(command, userUpdateEvent);
        eventQueue.enqueue(userUpdateEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, ResetPasswordCommand command) {
        try {
            checkUserIsExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new PasswordResetFailedEvent());
            return false;
        }
        PasswordResetEvent passwordResetEvent = new PasswordResetEvent();
        BeanUtils.copyProperties(command, passwordResetEvent);
        eventQueue.enqueue(passwordResetEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, ChangeStatusCommand command){
        try {
            checkUserIsExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new StatusChangeFailedEvent());
            return false;
        }
        StatusChangeEvent userStatusChangeEvent = new StatusChangeEvent();
        BeanUtils.copyProperties(command, userStatusChangeEvent);
        eventQueue.enqueue(userStatusChangeEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, DeleteUserCommand command){
        try {
            checkUserIsExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new UserDeleteFailedEvent());
            return false;
        }
        UserDeleteEvent userDeleteEvent = new UserDeleteEvent();
        BeanUtils.copyProperties(command, userDeleteEvent);
        eventQueue.enqueue(userDeleteEvent);
        return true;
    }

    public void checkUserIsExist() {
        if (userId == null) {
            throw new ApiException(ErrorCode.Business.USER_NON_EXIST);
        }
    }

    public void checkUsernameIsUnique() {
        if (!userNameIsUnique) {
            throw new ApiException(ErrorCode.Business.USER_NAME_IS_NOT_UNIQUE);
        }
    }

    public void checkPhoneNumberIsUnique() {
        if (!phoneNumberIsUnique) {
            {
                throw new ApiException(ErrorCode.Business.USER_PHONE_NUMBER_IS_NOT_UNIQUE);
            }
        }
    }

    public void checkEmailIsUnique() {
        if (!emailIsUnique) {
            throw new ApiException(ErrorCode.Business.USER_EMAIL_IS_NOT_UNIQUE);
        }
    }


    public void checkFieldRelatedEntityExist() {
        if (!postIsExist) {
            throw new ApiException(ErrorCode.Business.POST_IS_NOT_EXIST);
        }
        if (!deptIsExist) {
            throw new ApiException(ErrorCode.Business.DEPT_IS_NOT_EXIST);
        }
        if (!roleIsExist) {
            throw new ApiException(ErrorCode.Business.ROLE_IS_NOT_EXIST);
        }
    }


}
