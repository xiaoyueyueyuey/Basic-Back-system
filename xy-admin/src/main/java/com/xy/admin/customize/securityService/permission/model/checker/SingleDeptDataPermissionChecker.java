package com.xy.admin.customize.securityService.permission.model.checker;


import com.xy.admin.customize.securityService.permission.model.AbstractDataPermissionChecker;
import com.xy.admin.customize.securityService.permission.model.DataCondition;
import com.xy.admin.query.service.SysDeptService;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 数据权限测试接口
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleDeptDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;

    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        if (condition == null || loginUser == null) {
            return false;
        }

        if (loginUser.getDeptId() == null || condition.getTargetDeptId() == null) {
            return false;
        }

        Long currentDeptId = loginUser.getDeptId();
        Long targetDeptId = condition.getTargetDeptId();

        return Objects.equals(currentDeptId, targetDeptId);
    }


}
