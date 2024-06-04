package com.xy.domain.system.role.command;

import com.xy.domain.system.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@Data
@NoArgsConstructor
public class UpdateRoleStatusCommand implements Command {

    private Long roleId;

    private Integer status;

}
