package com.xy.admin.domain.repository;

import com.xy.admin.mapper.SysConfigMapper;
import com.xy.domain.system.config.ConfigModel;
import com.xy.domain.system.config.ConfigRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ConfigRepositoryImpl implements ConfigRepository {
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Override
    public ConfigModel findByIdOrError(Long id) {
        return null;
    }

    @Override
    public Boolean save(ConfigModel model) {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public Set<String> getConfigOptionSet(Integer configId) {
      return   sysConfigMapper.selectConfigOptionSet(configId);
    }
}
