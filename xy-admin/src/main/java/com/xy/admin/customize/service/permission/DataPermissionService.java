package com.xy.admin.customize.service.permission;

import cn.hutool.core.collection.CollUtil;
import com.xy.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import com.xy.admin.customize.service.permission.model.DataCondition;
import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.query.service.SysUserService;
import com.xy.infrastructure.user.AuthenticationUtils;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据权限校验服务
 * This service is responsible for checking data permissions.
 * @author valarchie
 */
@Service("dataScope")  // 声明这是一个Spring的Service组件，并将其命名为"dataScope"
@RequiredArgsConstructor  // 使用Lombok自动生成构造函数
public class DataPermissionService {

    private final SysUserService userService;  // 注入SysUserService依赖

    /**
     * 通过userId 校验当前用户 对 目标用户是否有操作权限
     * Check if the current user has permission to operate on the target user by userId.
     * @param userId 用户id (User ID)
     * @return 检验结果 (Check result)
     */
    public boolean checkUserId(Long userId) {  // 检查用户ID权限的方法
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();  // 获取当前登录用户信息
        SysUserEntity targetUser = userService.getById(userId);  // 根据用户ID获取目标用户信息
        if (targetUser == null) {  // 如果目标用户不存在
            return true;  // 返回true
        }
        return checkDataScope(loginUser, targetUser.getDeptId(), userId);  // 调用checkDataScope方法进行数据权限检查
    }

    /**
     * 通过userId 校验当前用户 对 目标用户是否有操作权限
     * Check if the current user has permission to operate on the target user by userIds.
     * @param userIds 用户id列表 (List of user IDs)
     * @return 校验结果 (Check result)
     */
    public boolean checkUserIds(List<Long> userIds) {  // 检查用户ID列表权限的方法
        if (CollUtil.isNotEmpty(userIds)) {  // 如果用户ID列表不为空
            for (Long userId : userIds) {  // 遍历用户ID列表
                boolean checkResult = checkUserId(userId);  // 对每个用户ID进行权限检查
                if (!checkResult) {  // 如果权限检查结果为false
                    return false;  // 返回false
                }
            }
        }
        return true;  // 返回true
    }

    /**
     * 通过deptId 校验当前用户 对 目标部门是否有操作权限
     * Check if the current user has permission to operate on the target department by deptId.
     * @param deptId 部门id (Department ID)
     * @return 检验结果 (Check result)
     */
    public boolean checkDeptId(Long deptId) {  // 检查部门ID权限的方法
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();  // 获取当前登录用户信息
        return checkDataScope(loginUser, deptId, null);  // 调用checkDataScope方法进行数据权限检查
    }

    /**
     * 通过部门ID或用户ID校验当前用户是否有操作权限
     * Check if the current user has permission to operate based on department ID or user ID.
     * @param loginUser 当前登录用户 (Current logged-in user)
     * @param targetDeptId 目标部门ID (Target department ID)
     * @param targetUserId 目标用户ID (Target user ID)
     * @return 检验结果 (Check result)
     */
    public boolean checkDataScope(SystemLoginUser loginUser, Long targetDeptId, Long targetUserId) {  // 检查数据权限范围的方法
        DataCondition dataCondition = DataCondition.builder().targetDeptId(targetDeptId).targetUserId(targetUserId).build();  // 创建数据条件对象
        AbstractDataPermissionChecker checker = DataPermissionCheckerFactory.getChecker(loginUser);  // 获取数据权限检查器
        return checker.check(loginUser, dataCondition);  // 调用数据权限检查器的check方法进行权限检查
    }
}
