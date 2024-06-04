package com.xy.admin.query.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.admin.dto.role.RoleDTO;
import com.xy.admin.dto.user.UserDTO;
import com.xy.admin.entity.SysMenuEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.query.AllocatedRoleQuery;
import com.xy.admin.query.UnallocatedRoleQuery;
import com.xy.infrastructure.page.PageDTO;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
public interface SysRoleService extends IService<SysRoleEntity> {


    /**
     * 校验角色名称是否唯一
     * @param roleId 角色Id
     * @param roleName 角色名称
     * @return 结果
     */
    boolean isRoleNameDuplicated(Long roleId, String roleName);

    /**
     * 校验角色权限是否唯一
     * @param roleId 角色Id
     * @param roleKey 角色的Key
     * @return 结果
     */
    boolean isRoleKeyDuplicated(Long roleId, String roleKey);


    /**
     * 检测角色是否分配给用户
     *
     * @param roleId 角色id
     * @return 校验结果
     */
    boolean isAssignedToUsers(Long roleId);

    /**
     * 获取用户的权限列表
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<SysMenuEntity> getMenuListByRoleId(Long roleId);


    RoleDTO getRoleInfo(Long roleId);

    PageDTO<UserDTO> getAllocatedUserList(AllocatedRoleQuery query);

    PageDTO<UserDTO> getUnallocatedUserList(UnallocatedRoleQuery query);
}
