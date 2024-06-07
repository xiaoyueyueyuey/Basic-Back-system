package com.xy.admin.domain.repository;

import com.xy.admin.mapper.SysConfigMapper;
import com.xy.domain.system.config.ConfigModel;
import com.xy.domain.system.config.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
@RequiredArgsConstructor

@Component
public class ConfigRepositoryImpl implements ConfigRepository {
    private final SysConfigMapper sysConfigMapper;
    @Override
    public ConfigModel findByIdOrError(Long id) {
        return null;
    }

    @Override
    public Long save(ConfigModel model) {
        return null;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Set<String> getConfigOptionSet(Integer configId) {
      return   sysConfigMapper.selectConfigOptionSet(configId);
    }
}
