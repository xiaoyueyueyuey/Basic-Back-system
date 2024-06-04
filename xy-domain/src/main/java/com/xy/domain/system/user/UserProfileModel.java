package com.xy.domain.system.user;

import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import com.xy.domain.system.user.command.user.UpdateUserAvatarCommand;
import com.xy.domain.system.user.command.user.UpdateUserPasswordCommand;
import com.xy.domain.system.user.command.user.UpdateUserProfileCommand;
import com.xy.domain.system.user.event.user.UserAvatarUpdateEvent;
import com.xy.domain.system.user.event.user.UserAvatarUpdateFailedEvent;
import com.xy.domain.system.user.event.user.UserProfileUpdateEvent;
import com.xy.domain.system.user.event.user.UserProfileUpdateFailedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class UserProfileModel {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
//
    private Boolean userNameIsUnique;
    private Boolean emailIsUnique;
    private Boolean phoneNumberIsUnique;
    private Boolean newOldPasswordIsEqual;

    public Boolean handle(EventQueue eventQueue, UpdateUserProfileCommand command) {
        try {
            checkUserIsExist();
            checkUsernameIsUnique();
            checkPhoneNumberIsUnique();
            checkEmailIsUnique();
            checkNewOldPasswordIsEqual();
        } catch (ApiException e) {
            eventQueue.enqueue(new UserProfileUpdateFailedEvent());
            return false;
        }
        UserProfileUpdateEvent userProfileUpdateEvent = new UserProfileUpdateEvent();
        BeanUtils.copyProperties(command, userProfileUpdateEvent);
        eventQueue.enqueue(userProfileUpdateEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, UpdateUserAvatarCommand command) {
        try {
            checkUserIsExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new UserAvatarUpdateFailedEvent());
            return false;
        }
        UserAvatarUpdateEvent userAvatarUpdateEvent = new UserAvatarUpdateEvent();
        BeanUtils.copyProperties(command, userAvatarUpdateEvent);
        eventQueue.enqueue(userAvatarUpdateEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, UpdateUserPasswordCommand command){
        try {
            checkUserIsExist();
            checkNewOldPasswordIsEqual();
        } catch (ApiException e) {
            eventQueue.enqueue(new UserProfileUpdateFailedEvent());
            return false;
        }
        UserProfileUpdateEvent userProfileUpdateEvent = new UserProfileUpdateEvent();
        BeanUtils.copyProperties(command, userProfileUpdateEvent);
        eventQueue.enqueue(userProfileUpdateEvent);
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
    public void checkNewOldPasswordIsEqual() {
        if (!newOldPasswordIsEqual) {
            throw new ApiException(ErrorCode.Business.USER_NEW_OLD_PASSWORD_IS_NOT_EQUAL);
        }
    }

}
