package com.xy.admin.query.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.entity.SysOperationLogEntity;
import com.xy.admin.mapper.SysOperationLogMapper;
import com.xy.admin.query.service.SysOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-08
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLogEntity> implements
        SysOperationLogService {

}
