package com.xy.domain.common;

import java.util.List;

public interface Repository <T> {
    T findByIdOrError(Long id);
    Boolean save(T model);
    Boolean deleteBatchByIds(List<Long> ids);
}
