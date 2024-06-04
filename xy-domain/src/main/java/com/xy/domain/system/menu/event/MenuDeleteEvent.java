package com.xy.domain.system.menu.event;

import com.xy.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MenuDeleteEvent implements DomainEvent {
    private Long menuId;
}
