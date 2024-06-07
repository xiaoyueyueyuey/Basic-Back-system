package com.xy.admin.customize.securityService.permission.model.checker;


import com.xy.admin.customize.securityService.permission.model.AbstractDataPermissionChecker;
import com.xy.admin.customize.securityService.permission.model.DataCondition;
import com.xy.admin.query.service.SysDeptService;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 部門数据权限测试接口
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DeptTreeDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;
    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        if (condition == null || loginUser == null) {// 条件为空或者用户为空，返回false
            return false;
        }
        if (loginUser.getDeptId() == null || condition.getTargetDeptId() == null) {// 用户部门为空或者目标部门为空，返回false
            return false;
        }
        Long currentDeptId = loginUser.getDeptId();// 当前用户部门
        Long targetDeptId = condition.getTargetDeptId();// 目标部门
        boolean isAdmin=loginUser.isAdmin();//TODO超管也有权限，便于测试，之后要去掉
        log.info("当前用户部门：{}，目标部门：{},用户是否是管理员{}",currentDeptId,targetDeptId,isAdmin);
        boolean isContainsTargetDept = deptService.isChildOfTheDept(loginUser.getDeptId(), targetDeptId);// 当前用户部门是否包含目标部门,包含才有操作权限
        boolean isSameDept = Objects.equals(currentDeptId, targetDeptId);// 是否是同一个部门,是也有操作权限
        return isContainsTargetDept || isSameDept||isAdmin;
    }

}
