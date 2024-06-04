package com.xy.domain.system.role;

import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import com.xy.domain.system.role.command.*;
import com.xy.domain.system.role.event.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;


@Data
@NoArgsConstructor
public class RoleModel  {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Long roleIsAssignToUserCount;
    private Integer status;
//
    private Boolean roleNameIsUnique;
    private Boolean roleKeyIsUnique;

    public Boolean handle(EventQueue eventQueue, AddRoleCommand command) {
        try {
            checkRoleNameUnique();
            checkRoleKeyUnique();
        } catch (ApiException e) {
            eventQueue.enqueue(new RoleAddFailedEvent());

            return false;
        }
        RoleAddEvent roleAddEvent = new RoleAddEvent();
        BeanUtils.copyProperties(command, roleAddEvent);
        eventQueue.enqueue(roleAddEvent);
        return true;
    }

    public Boolean handle(EventQueue eventQueue, UpdateRoleStatusCommand command) {
        try {
            checkRoleIsExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new RoleStatusUpdateFailedEvent());
            return false;
        }
        RoleStatusUpdateEvent roleAddEvent = new RoleStatusUpdateEvent();
        BeanUtils.copyProperties(command, roleAddEvent);
        eventQueue.enqueue(roleAddEvent);
        return true;
    }

    public Boolean handle(EventQueue eventQueue, UpdateRoleCommand command){
        try {
            checkRoleIsExist();
            checkRoleNameUnique();
            checkRoleKeyUnique();
        } catch (ApiException e) {
            eventQueue.enqueue(new RoleUpdateFailedEvent());

            return false;
        }
        RoleUpdateEvent roleUpdateEvent = new RoleUpdateEvent();
        BeanUtils.copyProperties(command, roleUpdateEvent);
        eventQueue.enqueue(roleUpdateEvent);
        return true;
    }

    public Boolean handle(EventQueue eventQueue, DeleteRoleCommand command){
        try {
            checkRoleIsExist();
            checkRoleCanBeDelete();
        } catch (ApiException e) {
            eventQueue.enqueue(new RoleDeleteFailedEvent());
            return false;
        }
        RoleDeleteEvent roleDeleteEvent = new RoleDeleteEvent();
        BeanUtils.copyProperties(command, roleDeleteEvent);
        eventQueue.enqueue(roleDeleteEvent);
        return true;
    }

    public Boolean handle(EventQueue eventQueue, UpdateRoleDataScopeCommand command){
        try {
            checkRoleIsExist();
        } catch (ApiException e) {
            eventQueue.enqueue(new RoleDataScopeUpdateFailedEvent());
            return false;
        }
        RoleDataScopeUpdateEvent roleDataScopeUpdateEvent = new RoleDataScopeUpdateEvent();
        BeanUtils.copyProperties(command, roleDataScopeUpdateEvent);
        eventQueue.enqueue(roleDataScopeUpdateEvent);
        return true;
    }

    public void checkRoleIsExist() {
        if (roleId == null) {
            throw new ApiException(ErrorCode.Business.ROLE_IS_NOT_EXIST);
        }
    }
    public void checkRoleNameUnique() {
        if (!roleNameIsUnique) {
            throw new ApiException(ErrorCode.Business.ROLE_NAME_IS_NOT_UNIQUE, getRoleName());
        }
    }
    public void checkRoleCanBeDelete() {
        if (roleIsAssignToUserCount > 0) {
            throw new ApiException(ErrorCode.Business.ROLE_ALREADY_ASSIGN_TO_USER, getRoleName());
        }
    }
    public void checkRoleKeyUnique() {
        if (!roleKeyIsUnique) {
            throw new ApiException(ErrorCode.Business.ROLE_KEY_IS_NOT_UNIQUE, getRoleKey());
        }
    }

//    public void checkRoleAvailable() {
//        if (StatusEnum.DISABLE.getValue().equals(getStatus())) {
//            throw new ApiException(ErrorCode.Business.ROLE_IS_NOT_AVAILABLE, getRoleName());
//        }
//    }

//    public void generateDeptIdSet() {
//        if (deptIds == null) {
//            setDeptIdSet("");
//            return;
//        }
//        if (deptIds.size() > new HashSet<>(deptIds).size()) {
//            throw new ApiException(ErrorCode.Business.ROLE_DATA_SCOPE_DUPLICATED_DEPT);
//        }
//        String deptIdSet = StrUtil.join(",", deptIds);
//        setDeptIdSet(deptIdSet);
//    }



//    @Override
//    public boolean insert() {
//        super.insert();
//        return saveMenus();
//    }
//
//    @Override
//    public boolean updateById() {
//        // 清空之前的角色菜单关联
//        cleanOldMenus();
//        saveMenus();
//        return super.updateById();
//    }
//
//    @Override
//    public boolean deleteById() {
//        // 清空之前的角色菜单关联
//        cleanOldMenus();
//        return super.deleteById();
//    }
//
//    private void cleanOldMenus() {
//        LambdaQueryWrapper<SysRoleMenuEntity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysRoleMenuEntity::getRoleId, getRoleId());
//
//        roleMenuService.remove(queryWrapper);
//    }
//
//    private boolean saveMenus() {
//        List<SysRoleMenuEntity> list = new ArrayList<>();
//        if (getMenuIds() != null) {
//            for (Long menuId : getMenuIds()) {
//                SysRoleMenuEntity rm = new SysRoleMenuEntity();
//                rm.setRoleId(getRoleId());
//                rm.setMenuId(menuId);
//                list.add(rm);
//            }
//            return roleMenuService.saveBatch(list);
//        }
//        return false;
//    }
}
