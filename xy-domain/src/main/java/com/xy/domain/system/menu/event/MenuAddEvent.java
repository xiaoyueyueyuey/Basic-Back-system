package com.xy.domain.system.menu.event;

import com.xy.domain.DomainEvent;
import com.xy.domain.system.menu.dto.MetaDTO;
import lombok.Data;

@Data
public class MenuAddEvent implements DomainEvent {
    private Long parentId;
    private String menuName;
    private String routerName;
    private String path;
    private Integer status;
    private Integer menuType;
    private Boolean isButton;
    private String permission;
    private MetaDTO meta;
    private String remark;
}
