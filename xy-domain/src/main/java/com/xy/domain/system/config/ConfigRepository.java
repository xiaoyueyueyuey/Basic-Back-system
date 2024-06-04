package com.xy.domain.system.config;

import com.xy.domain.common.Repository;

import java.util.Set;

public interface ConfigRepository extends Repository<ConfigModel>{
    Set<String> getConfigOptionSet(Integer configId);
}
