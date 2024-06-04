package com.xy.admin.customize.service.permission.model.checker;

import com.xy.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import com.xy.admin.customize.service.permission.model.DataCondition;
import com.xy.admin.query.service.SysDeptService;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据权限测试接口
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AllDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;


    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        return true;
    }
}