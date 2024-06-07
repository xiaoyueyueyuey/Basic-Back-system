package com.xy.domain.system.notice.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.notice.NoticeModel;
import com.xy.domain.system.notice.command.AddNoticeCommand;
import org.springframework.stereotype.Component;

@Component
public class NoticeAddCommandHandler implements CommandHandler<AddNoticeCommand> {

    @Override
    public Boolean handle(EventQueue eventQueue, AddNoticeCommand command) {
       //新增就不需要找聚合了，直接new
        NoticeModel noticeModel = new NoticeModel();
        return noticeModel.handler(eventQueue, command);
    }
}
