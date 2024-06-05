package com.xy.domain.system.log.login.command;

import com.xy.domain.system.Command;
import lombok.Data;

import java.util.List;

@Data
public class DeleteLoginLogCommand implements Command {
    private List<Long> infoIds;
}
