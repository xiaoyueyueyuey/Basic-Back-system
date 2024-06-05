package com.xy.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.admin.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Mapper

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuEntity> {

}
