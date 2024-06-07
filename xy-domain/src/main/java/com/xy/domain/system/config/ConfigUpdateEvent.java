package com.xy.domain.system.config;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class ConfigUpdateEvent implements DomainEvent {
    private Integer configId;
    private String configValue;

}
