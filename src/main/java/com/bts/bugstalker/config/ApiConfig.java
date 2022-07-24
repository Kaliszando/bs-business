package com.bts.bugstalker.config;

import com.bts.bugstalker.util.properties.ApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
public class ApiConfig {

    private final ApiProperties apiProperties;

    @Bean
    public OpenAPI openApiConfig() {
        return new OpenAPI()
                .servers(provideServers())
                .info(new Info()
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

    private List<Server> provideServers() {
        return apiProperties.getServers().stream()
                .map(serverUrl -> new Server().url(serverUrl))
                .collect(Collectors.toList());
    }
}
