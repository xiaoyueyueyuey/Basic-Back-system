package com.xy.admin.domain.repository;

import com.xy.domain.system.log.operation.OperationLogModel;
import com.xy.domain.system.log.operation.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class OperationLogRepositoryImpl implements OperationLogRepository {
    @Override
    public OperationLogModel findByIdOrError(Long id) {
        return null;
    }

    @Override
    public Long save(OperationLogModel model) {
        return null;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return null;
    }
}
