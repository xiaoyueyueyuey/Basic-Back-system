package com.xy.domain.system.config;

import com.xy.domain.system.Command;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


/**
 * @author valarchie
 */
@Data
@Schema
public class UpdateConfigCommand implements Command {
    @NotNull
    @Positive
    private Integer configId;
    @NotNull
    @NotEmpty
    private String configValue;
}
