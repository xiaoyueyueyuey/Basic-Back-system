package com.xy.admin.domain.common;

import com.xy.domain.DomainEvent;
import com.xy.domain.EventQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * 事件队列
 */
public class SimpleEventQueue implements EventQueue {

    private final List<DomainEvent> queue = new LinkedList<>();

    @Override
    public void enqueue(DomainEvent event) {
        queue.add(event);

    }

    @Override
    public List<DomainEvent> queue() {
        return queue;
    }
}
