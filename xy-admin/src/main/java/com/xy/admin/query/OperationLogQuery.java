package com.xy.admin.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.admin.entity.SysOperationLogEntity;
import com.xy.infrastructure.page.AbstractPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperationLogQuery extends AbstractPageQuery<SysOperationLogEntity> {

    private String businessType;
    private String status;
    private String username;
    private String requestModule;

    @Override
    public QueryWrapper<SysOperationLogEntity> addQueryCondition() {
        QueryWrapper<SysOperationLogEntity> queryWrapper = new QueryWrapper<SysOperationLogEntity>()
            .like(businessType!=null, "business_type", businessType)
            .eq(status != null, "status", status)
            .like(StrUtil.isNotEmpty(username), "username", username)
            .like(StrUtil.isNotEmpty(requestModule), "request_module", requestModule);

        this.timeRangeColumn = "operation_time";

        return queryWrapper;
    }
}
