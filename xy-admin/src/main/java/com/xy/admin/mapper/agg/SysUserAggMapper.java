package com.xy.admin.mapper.agg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.admin.entity.agg.SysUserAggEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserAggMapper extends BaseMapper<SysUserAggEntity> {
    String getPasswordByUserId(@Param("userId") Long userId);
}
