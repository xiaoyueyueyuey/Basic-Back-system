package com.xy.domain.system.config;

import cn.hutool.core.util.StrUtil;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Set;


@Data
public class ConfigModel {
    private Integer configId;
    private Set<String> configOptionSet;
    private String configValue;

    public Boolean handle(EventQueue eventQueue, UpdateConfigCommand command){
        try {
            checkConfigIsExist();
            checkCanBeModify();
        } catch (ApiException e) {
            eventQueue.enqueue(new ConfigUpdateFailedEvent());
            return false;
        }
        ConfigUpdateEvent configUpdateEvent = new ConfigUpdateEvent();
        BeanUtils.copyProperties(command, configUpdateEvent);
        eventQueue.enqueue(configUpdateEvent);
        return true;
    }
    public void checkConfigIsExist(){
        if(configId == null){
            throw new ApiException(ErrorCode.Business.CONFIG_IS_NOT_EXIST);
        }
    }

    public void checkCanBeModify() {
        if (StrUtil.isBlank(getConfigValue())) {
            throw new ApiException(ErrorCode.Business.CONFIG_VALUE_IS_NOT_ALLOW_TO_EMPTY);
        }

        if (!configOptionSet.isEmpty() && !configOptionSet.contains(getConfigValue())) {
            throw new ApiException(ErrorCode.Business.CONFIG_VALUE_IS_NOT_IN_OPTIONS);
        }
    }

}
