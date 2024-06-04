package com.xy.domain.system.user.command.manager;

import com.xy.domain.system.Command;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class ResetPasswordCommand  implements Command {

    private Long userId;
    private String password;

}
