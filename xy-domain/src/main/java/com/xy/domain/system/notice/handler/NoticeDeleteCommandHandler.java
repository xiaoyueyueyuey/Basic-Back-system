package com.xy.domain.system.notice.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.notice.NoticeModel;
import com.xy.domain.system.notice.NoticeRepository;
import com.xy.domain.system.notice.command.DeleteNoticeCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class NoticeDeleteCommandHandler implements CommandHandler<DeleteNoticeCommand> {
    @Resource
    private NoticeRepository noticeRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, DeleteNoticeCommand command) {
        //先找到聚合
        NoticeModel noticeModel = noticeRepository.findByIdOrError(command.getNoticeId());
        //处理命令
        return noticeModel.handle(eventQueue, command);

    }
}
