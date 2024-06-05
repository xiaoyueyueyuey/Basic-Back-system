package com.xy.domain.system.log.login.command;

import com.xy.domain.system.Command;
import lombok.Data;

import java.util.Date;

@Data
public class AddLoginLogCommand implements Command {

    private String username;
    private String ipAddress;
    private String loginLocation;
    private String browser;
    private String operationSystem;
    private Integer status;
    private String msg;
    private Date loginTime;


}
