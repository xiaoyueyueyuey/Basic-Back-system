package com.xy.domain.system.user.command.manager;

import com.xy.domain.system.Command;
import lombok.Data;

@Data
public class DeleteUserCommand implements Command {

        private Long userId;
}
