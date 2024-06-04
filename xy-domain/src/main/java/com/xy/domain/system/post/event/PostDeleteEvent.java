package com.xy.domain.system.post.event;


import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class PostDeleteEvent implements DomainEvent {

    private Long postId;
}
