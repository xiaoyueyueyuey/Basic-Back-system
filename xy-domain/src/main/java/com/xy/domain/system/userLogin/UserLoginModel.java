package com.xy.domain.system.userLogin;

import com.xy.domain.EventQueue;
import com.xy.domain.system.userLogin.command.UserLoginCommand;
import com.xy.domain.system.userLogin.event.UserLoginEvent;

public class UserLoginModel {
    public Boolean handle(EventQueue eventQueue, UserLoginCommand command) {
        UserLoginEvent userLoginEvent = new UserLoginEvent();
        eventQueue.enqueue(userLoginEvent);
        return true;
    }
}
