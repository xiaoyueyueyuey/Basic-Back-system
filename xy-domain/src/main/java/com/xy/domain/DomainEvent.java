package com.xy.domain;


public interface DomainEvent {
//  TODO //每个事件都对应一个聚合根,这里主要是用于增操作,到时再把事件通用接口拆分吧
    default void setAggregateId(Long aggregateId){};
}
