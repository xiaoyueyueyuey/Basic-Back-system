package com.xy.admin.domain.repository;

import com.xy.admin.entity.SysNoticeEntity;
import com.xy.admin.mapper.SysNoticeMapper;
import com.xy.domain.system.notice.NoticeModel;
import com.xy.domain.system.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class NoticeRepositoryImpl implements NoticeRepository {
    private final SysNoticeMapper sysNoticeMapper;

    @Override
    public NoticeModel findByIdOrError(Long id) {
        SysNoticeEntity sysNoticeEntity = sysNoticeMapper.selectById(id);
        if(sysNoticeEntity == null) {
            return new NoticeModel();
        }
        NoticeModel noticeModel = new NoticeModel();
        BeanUtils.copyProperties(sysNoticeEntity, noticeModel);
        return noticeModel;
    }

    //不用保存，事件已经包括了聚合，事件保存就好了
    @Override
    public Boolean save(NoticeModel model) {
        return null;

    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return sysNoticeMapper.deleteBatchIds(ids) > 0;
    }
}
