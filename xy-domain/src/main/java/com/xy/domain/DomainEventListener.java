package com.xy.domain;

public interface DomainEventListener {
     void onEvent(DomainEvent event);
}
