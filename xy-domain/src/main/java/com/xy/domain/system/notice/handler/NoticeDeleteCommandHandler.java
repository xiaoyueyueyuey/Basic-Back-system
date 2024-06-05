package com.xy.domain.system.notice.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.notice.NoticeModel;
import com.xy.domain.system.notice.command.DeleteNoticeCommand;
import org.springframework.stereotype.Component;

@Component
public class NoticeDeleteCommandHandler implements CommandHandler<DeleteNoticeCommand> {
    @Override
    public Boolean handle(EventQueue eventQueue, DeleteNoticeCommand command) {
        NoticeModel noticeModel = new NoticeModel();
        //处理命令
        return noticeModel.handle(eventQueue, command);
    }
}
