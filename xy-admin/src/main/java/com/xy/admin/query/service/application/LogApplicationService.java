package com.xy.admin.query.service.application;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.admin.common.command.BulkOperationCommand;
import com.xy.admin.dto.log.LoginLogDTO;
import com.xy.admin.dto.log.OperationLogDTO;
import com.xy.admin.entity.SysLoginInfoEntity;
import com.xy.admin.entity.SysOperationLogEntity;
import com.xy.admin.query.LoginLogQuery;
import com.xy.admin.query.OperationLogQuery;
import com.xy.admin.query.service.SysLoginInfoService;
import com.xy.admin.query.service.SysOperationLogService;
import com.xy.infrastructure.page.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class LogApplicationService {
    // TODO 命名到时候统一改成叫LoginLog
    private final SysLoginInfoService loginInfoService;

    private final SysOperationLogService operationLogService;

    public PageDTO<LoginLogDTO> getLoginInfoList(LoginLogQuery query) {
        Page<SysLoginInfoEntity> page = loginInfoService.page(query.toPage(), query.toQueryWrapper());
        List<LoginLogDTO> records = page.getRecords().stream().map(LoginLogDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public void deleteLoginInfo(BulkOperationCommand<Long> deleteCommand) {
        QueryWrapper<SysLoginInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("info_id", deleteCommand.getIds());
        loginInfoService.remove(queryWrapper);
    }
    public PageDTO<OperationLogDTO> getOperationLogList(OperationLogQuery query) {
        Page<SysOperationLogEntity> page = operationLogService.page(query.toPage(), query.toQueryWrapper());
        List<OperationLogDTO> records = page.getRecords().stream().map(OperationLogDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }
    public void deleteOperationLog(BulkOperationCommand<Long> deleteCommand) {
        operationLogService.removeBatchByIds(deleteCommand.getIds());
    }

}
