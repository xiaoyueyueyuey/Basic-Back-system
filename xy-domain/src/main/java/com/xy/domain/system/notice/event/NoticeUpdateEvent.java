package com.xy.domain.system.notice.event;

import lombok.Data;

@Data
public class NoticeUpdateEvent   {
    private Long noticeId;
    protected String noticeTitle;
    protected Integer noticeType;
    protected String noticeContent;
    protected Integer status;
    protected String remark;

}
