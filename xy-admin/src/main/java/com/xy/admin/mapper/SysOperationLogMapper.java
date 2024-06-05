package com.xy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.admin.entity.SysOperationLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2022-06-08
 */
@Mapper

public interface SysOperationLogMapper extends BaseMapper<SysOperationLogEntity> {

}
