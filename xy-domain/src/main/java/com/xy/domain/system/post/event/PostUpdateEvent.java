package com.xy.domain.system.post.event;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class PostUpdateEvent implements DomainEvent {
    private Long postId;
    private String postCode;

    private String postName;

    private Integer postSort;
    private String remark;
    private Integer status;
}
