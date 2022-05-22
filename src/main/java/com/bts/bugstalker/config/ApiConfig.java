package com.bts.bugstalker.config;

import com.bts.bugstalker.util.properties.ApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ApiConfig {

    private final ApiProperties apiProperties;

    @Bean
    public OpenAPI openApiConfig() {
        return new OpenAPI().info(new Info()
                .title(apiProperties.getTitle())
                .description(apiProperties.getDescription())
                .version(apiProperties.getVersion())
        );
    }

    @Bean
    public GroupedOpenApi storeOpenApi() {
        String[] paths = apiProperties.getExposed();
        return GroupedOpenApi.builder()
                .group(apiProperties.getGroup())
                .pathsToMatch(paths)
                .build();
    }

}