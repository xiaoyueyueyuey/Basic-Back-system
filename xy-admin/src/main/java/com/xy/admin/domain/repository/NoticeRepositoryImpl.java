package com.xy.admin.domain.repository;

import com.xy.admin.entity.SysNoticeEntity;
import com.xy.admin.mapper.SysNoticeMapper;
import com.xy.domain.system.notice.NoticeModel;
import com.xy.domain.system.notice.NoticeRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;

public class NoticeRepositoryImpl implements NoticeRepository {
    @Resource
    private SysNoticeMapper sysNoticeMapper;

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
    public Boolean deleteById(Long id) {
        return sysNoticeMapper.deleteById(id) > 0;
    }
}
