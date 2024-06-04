package com.xy.domain;

import java.util.List;

public interface EventQueue {
    void enqueue(DomainEvent event);
    List<DomainEvent> queue();
}
