package com.xy.infrastructure.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Mybatis支持*匹配扫描包
 *
 * @author valarchie
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig {
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        // 创建数据源事务管理器实例
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        // 设置数据源
        transactionManager.setDataSource(dataSource);
        // 返回数据源事务管理器实例
        return transactionManager;
    }
}
