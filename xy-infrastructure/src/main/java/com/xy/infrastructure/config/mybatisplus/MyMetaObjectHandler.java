package com.xy.infrastructure.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MP自动填充配置
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "createTime", LocalDate::now, LocalDate.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDate::now, LocalDate.class);
        this.strictInsertFill(metaObject, "status", () -> false, Boolean.class);
        this.strictInsertFill(metaObject, "status", () -> (short) 0, Short.class);
        this.strictInsertFill(metaObject, "deleted", () -> false, Boolean.class);
        this.strictInsertFill(metaObject, "remark", () -> "", String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }

}
