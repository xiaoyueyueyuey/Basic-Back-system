package com.xy.domain.system.menu.command;

import com.xy.domain.system.Command;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteMenuCommand implements Command {
    @NotNull
    private Long menuId;
}
