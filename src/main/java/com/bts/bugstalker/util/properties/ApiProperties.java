package com.bts.bugstalker.util.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bts.api")
public class ApiProperties {

    private String path;

    private String[] exposed;

    private String group;

    private String title;

    private String description;

    private String version;

}
