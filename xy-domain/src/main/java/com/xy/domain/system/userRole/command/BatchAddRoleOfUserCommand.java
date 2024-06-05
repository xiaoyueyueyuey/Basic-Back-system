package com.xy.domain.system.userRole.command;

import com.xy.domain.system.Command;
import lombok.Data;

import java.util.List;

@Data
public class BatchAddRoleOfUserCommand implements Command {
    List<Long> userIds;
    List<Long> roleIds;
}
