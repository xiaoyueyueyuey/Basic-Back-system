package com.xy.infrastructure.config.springdoc;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author valarchie
 * SpringDoc API文档相关配置
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI xyBootApi() {
        return new OpenAPI()
                .info(new Info().title("Xyboot后台管理系统")
                        .description("Xyboot API 演示")
                        .version("v1.8.0")
                        .license(new License().name("MIT 3.0").url("https://github.com/valarchie/XYBoot-Basic")))
                .externalDocs(new ExternalDocumentation()
                        .description("Xyboot后台管理系统接口文档")
                        .url("https://juejin.cn/column/7159946528827080734"));
    }

}
