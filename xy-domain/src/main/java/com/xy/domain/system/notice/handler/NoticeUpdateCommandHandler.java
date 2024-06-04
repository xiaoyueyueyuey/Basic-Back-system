package com.xy.domain.system.notice.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.notice.NoticeModel;
import com.xy.domain.system.notice.NoticeRepository;
import com.xy.domain.system.notice.command.UpdateNoticeCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class NoticeUpdateCommandHandler implements CommandHandler<UpdateNoticeCommand> {
    @Resource
    NoticeRepository noticeRepository;

    @Override
    public Boolean handle(EventQueue eventQueue, UpdateNoticeCommand command) {
        NoticeModel model = noticeRepository.findByIdOrError(command.getNoticeId());
        return model.handler(eventQueue, command);
    }

}
