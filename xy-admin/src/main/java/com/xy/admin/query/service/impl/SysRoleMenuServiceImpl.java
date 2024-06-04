package com.xy.admin.query.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.entity.SysRoleMenuEntity;
import com.xy.admin.mapper.SysRoleMenuMapper;
import com.xy.admin.query.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuEntity> implements
        SysRoleMenuService {

}
