package com.xy.domain.system.user.command.user;

import com.xy.domain.system.Command;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class UpdateUserPasswordCommand implements Command {

    private Long userId;
    private String newPassword;
    private String oldPassword;

}
