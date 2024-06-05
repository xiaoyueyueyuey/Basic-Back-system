package com.xy.admin.query.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.dto.log.LoginLogDTO;
import com.xy.admin.entity.SysLoginInfoEntity;
import com.xy.admin.mapper.SysLoginInfoMapper;
import com.xy.admin.query.LoginLogQuery;
import com.xy.admin.query.service.SysLoginInfoService;
import com.xy.infrastructure.page.PageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-07-10
 */
@Service
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoMapper, SysLoginInfoEntity> implements
        SysLoginInfoService {

    @Override
    public PageDTO<LoginLogDTO> getLoginInfoList(LoginLogQuery query) {
        Page<SysLoginInfoEntity> page = this.page(query.toPage(), query.toQueryWrapper());
        List<LoginLogDTO> records = page.getRecords().stream().map(LoginLogDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }
}
