package com.xy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.admin.entity.SysLoginInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统访问记录 Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2022-06-06
 */
@Mapper

public interface SysLoginInfoMapper extends BaseMapper<SysLoginInfoEntity> {

}
