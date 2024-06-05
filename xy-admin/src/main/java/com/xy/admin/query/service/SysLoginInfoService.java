package com.xy.admin.query.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.admin.dto.log.LoginLogDTO;
import com.xy.admin.entity.SysLoginInfoEntity;
import com.xy.admin.query.LoginLogQuery;
import com.xy.infrastructure.page.PageDTO;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-06
 */
public interface SysLoginInfoService extends IService<SysLoginInfoEntity> {

    PageDTO<LoginLogDTO> getLoginInfoList(LoginLogQuery query);
}
