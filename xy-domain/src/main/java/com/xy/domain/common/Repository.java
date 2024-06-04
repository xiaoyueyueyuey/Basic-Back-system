package com.xy.domain.common;

public interface Repository <T> {
    T findByIdOrError(Long id);
    Boolean save(T model);
    Boolean deleteById(Long id);
}
