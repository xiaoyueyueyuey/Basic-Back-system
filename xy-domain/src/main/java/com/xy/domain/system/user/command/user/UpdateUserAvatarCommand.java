package com.xy.domain.system.user.command.user;

import com.xy.domain.system.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@Data
@NoArgsConstructor
public class UpdateUserAvatarCommand implements Command {
    public UpdateUserAvatarCommand(Long userId, String avatar) {
        this.userId = userId;
        this.avatar = avatar;
    }
    private Long userId;
    private String avatar;

}
