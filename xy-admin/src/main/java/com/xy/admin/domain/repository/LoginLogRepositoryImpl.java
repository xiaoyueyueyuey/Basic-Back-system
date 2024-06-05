package com.xy.admin.domain.repository;

import com.xy.domain.system.log.login.LoginLogModel;
import com.xy.domain.system.log.login.LoginLogRepository;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class LoginLogRepositoryImpl implements LoginLogRepository {
    @Override
    public LoginLogModel findByIdOrError(Long id) {
        return null;
    }

    @Override
    public Boolean save(LoginLogModel model) {
        return null;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return null;
    }
}
