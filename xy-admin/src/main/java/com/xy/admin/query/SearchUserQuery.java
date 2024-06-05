package com.xy.admin.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.infrastructure.page.AbstractPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当出现复用Query的情况，我们需要把泛型加到类本身，通过传入类型 来进行复用
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchUserQuery<T> extends AbstractPageQuery<T> {

    protected Long userId;
    protected String username;
    protected Integer status;
    protected String phoneNumber;
    protected Long deptId;

    @Override
    public QueryWrapper<T> addQueryCondition() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 设置查询条件
        queryWrapper.like(StrUtil.isNotEmpty(username), "username", username)//模糊查询
            .like(StrUtil.isNotEmpty(phoneNumber), "u.phone_number", phoneNumber)//模糊查询
            .eq(userId != null, "u.user_id", userId)//精确查询
            .eq(status != null, "u.status", status)//精确查询
            .eq("u.deleted", 0)//精确查询
            .and(deptId != null, o ->//部门查询
                o.eq("u.dept_id", deptId)//精确查询
                    .or()
                    .apply("u.dept_id IN ( SELECT t.dept_id FROM sys_dept t WHERE find_in_set(" + deptId
                        + ", ancestors))"));//模糊查询

        // 设置排序字段
        this.timeRangeColumn = "u.create_time";

        return queryWrapper;
    }
}
