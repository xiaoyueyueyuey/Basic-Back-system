package com.xy.admin.query.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.dto.log.OperationLogDTO;
import com.xy.admin.entity.SysOperationLogEntity;
import com.xy.admin.mapper.SysOperationLogMapper;
import com.xy.admin.query.OperationLogQuery;
import com.xy.admin.query.service.SysOperationLogService;
import com.xy.infrastructure.page.PageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageDTO<OperationLogDTO> getOperationLogList(OperationLogQuery query) {
        Page<SysOperationLogEntity> page = this.page(query.toPage(), query.toQueryWrapper());
        List<OperationLogDTO> records = page.getRecords().stream().map(OperationLogDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }
}
