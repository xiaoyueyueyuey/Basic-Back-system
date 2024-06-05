package com.xy.admin.query.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.admin.dto.log.OperationLogDTO;
import com.xy.admin.entity.SysOperationLogEntity;
import com.xy.admin.query.OperationLogQuery;
import com.xy.infrastructure.page.PageDTO;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-08
 */
public interface SysOperationLogService extends IService<SysOperationLogEntity> {

    PageDTO<OperationLogDTO> getOperationLogList(OperationLogQuery query);
}
