package com.xy.domain.system.notice.command;

import com.xy.domain.system.Command;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DeleteNoticeCommand implements Command {
    @NotNull
    @Positive
    private Long noticeId;
}
