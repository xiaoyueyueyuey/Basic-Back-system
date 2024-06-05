package com.xy.admin.query.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.dto.role.RoleDTO;
import com.xy.admin.dto.user.UserDTO;
import com.xy.admin.entity.SysMenuEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.mapper.SysRoleMapper;
import com.xy.admin.query.AllocatedRoleQuery;
import com.xy.admin.query.UnallocatedRoleQuery;
import com.xy.admin.query.service.SysMenuService;
import com.xy.admin.query.service.SysRoleService;
import com.xy.admin.query.service.SysUserService;
import com.xy.infrastructure.page.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    private final SysMenuService menuService;

    private final SysUserService userService;

    //    @Override
//    public boolean isRoleNameDuplicated(Long roleId, String roleName) {
//        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ne(roleId != null, "role_id", roleId)
//            .eq("role_name", roleName);
//        return this.baseMapper.exists(queryWrapper);
//    }
//    @Override
//    public boolean isRoleKeyDuplicated(Long roleId, String roleKey) {
//        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ne(roleId != null, "role_id", roleId)
//            .eq("role_key", roleKey);
//        return this.baseMapper.exists(queryWrapper);
//    }
//    @Override
//    public boolean isAssignedToUsers(Long roleId) {
//        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id", roleId);
//        return userMapper.exists(queryWrapper);
//    }
    @Override
    public List<SysMenuEntity> getMenuListByRoleId(Long roleId) {
        return baseMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public RoleDTO getRoleInfo(Long roleId) {
        SysRoleEntity byId = this.getById(roleId);
        RoleDTO roleDTO = new RoleDTO(byId);
        List<Long> selectedDeptList = StrUtil.split(byId.getDeptIdSet(), ",")
                .stream().filter(StrUtil::isNotEmpty).map(
                        s -> Long.parseLong(s.trim())
                ).collect(Collectors.toList());
        roleDTO.setSelectedDeptList(selectedDeptList);
        roleDTO.setSelectedMenuList(menuService.getMenuIdsByRoleId(roleId));
        return roleDTO;
    }

    @Override
    public PageDTO<UserDTO> getAllocatedUserList(AllocatedRoleQuery query) {
        Page<SysUserEntity> page = userService.getUserListByRole(query);
        List<UserDTO> dtoList = page.getRecords().stream().map(UserDTO::new).collect(Collectors.toList());
        return new PageDTO<>(dtoList, page.getTotal());
    }

    @Override
    public PageDTO<UserDTO> getUnallocatedUserList(UnallocatedRoleQuery query) {
        Page<SysUserEntity> page = userService.getUserListByRole(query);
        List<UserDTO> dtoList = page.getRecords().stream().map(UserDTO::new).collect(Collectors.toList());
        return new PageDTO<>(dtoList, page.getTotal());
    }


}
