package com.xy.domain.system.role.command;

import com.xy.domain.system.Command;
import lombok.Data;

@Data
public class DeleteRoleCommand implements Command {
        private Long roleId;
}
