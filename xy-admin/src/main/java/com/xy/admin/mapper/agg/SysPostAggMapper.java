package com.xy.admin.mapper.agg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.admin.entity.agg.SysPostAggEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysPostAggMapper extends BaseMapper<SysPostAggEntity> {
    Integer selectPostIsAssignedCount(@Param("postId") Long postId);
}
