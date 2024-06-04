package com.xy.domain.system.dept.command;

import com.xy.domain.system.Command;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DeleteDeptCommand implements Command {
    @NotNull
    @PositiveOrZero
    private Long deptId;
}
