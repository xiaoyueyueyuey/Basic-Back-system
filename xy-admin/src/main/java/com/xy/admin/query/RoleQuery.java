package com.xy.admin.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.infrastructure.page.AbstractPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQuery extends AbstractPageQuery<SysRoleEntity> {

    private String roleName;

    private String roleKey;

    private String status;


    @Override
    public QueryWrapper<SysRoleEntity> addQueryCondition() {
        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>()
            .eq(status != null, "status", status)
            .eq(StrUtil.isNotEmpty(roleKey), "role_key", roleKey)
            .like(StrUtil.isNotEmpty(roleName), "role_name", roleName);
//        this.addTimeCondition(queryWrapper, "create_time");
//        this.setOrderColumn("role_sort");
//        this.addSortCondition(queryWrapper);

        return queryWrapper;
    }
}
