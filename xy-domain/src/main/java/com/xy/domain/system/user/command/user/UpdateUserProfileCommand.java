package com.xy.domain.system.user.command.user;

import com.xy.domain.system.Command;
import lombok.Data;

/**
 * @author valarchie
 */

/**
 * 修改用户正常信息
 */
@Data
public class UpdateUserProfileCommand implements Command {
    private Long userId;
    private Integer sex;
    private String username;
    private String nickName;
    private String phoneNumber;
    private String email;
}
