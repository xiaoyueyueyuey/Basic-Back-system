package com.xy.domain.common;

import java.util.List;

public interface Repository <T> {
    T findByIdOrError(Long id);
    Long save(T model);//返回新增的主键，更新返回什么自己定义
    Boolean deleteBatchByIds(List<Long> ids);
}
