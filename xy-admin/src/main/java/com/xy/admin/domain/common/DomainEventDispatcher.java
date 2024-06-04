package com.xy.admin.domain.common;

import com.xy.domain.EventQueue;

public interface DomainEventDispatcher {
    void dispatchNow(EventQueue eventQueue);
}
