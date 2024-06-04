package com.xy.admin.query.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.admin.dto.config.ConfigDTO;
import com.xy.admin.entity.SysConfigEntity;
import com.xy.admin.query.ConfigQuery;
import com.xy.infrastructure.page.PageDTO;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-09
 */
public interface SysConfigService extends IService<SysConfigEntity> {

    /**
     * 通过key获取配置
     *
     * @param key 配置对应的key
     * @return 配置
     */
    String getConfigValueByKey(String key);

    PageDTO<ConfigDTO> getConfigList(ConfigQuery query);

    ConfigDTO getConfigInfo(Integer configId);
}
