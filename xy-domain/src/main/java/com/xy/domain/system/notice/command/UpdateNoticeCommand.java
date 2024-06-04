package com.xy.domain.system.notice.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateNoticeCommand extends AddNoticeCommand {

    @NotNull
    @Positive
    protected Long noticeId;

}
