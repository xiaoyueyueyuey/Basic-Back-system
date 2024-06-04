package com.xy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.admin.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 参数配置表 Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2022-06-09
 */
public interface SysConfigMapper extends BaseMapper<SysConfigEntity> {

    Set<String> selectConfigOptionSet(@Param("configId") Integer configId);
}
