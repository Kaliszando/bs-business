package com.bts.bugstalker.util.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter @RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "bs.api")
public final class ApiProperties {

    private final String path;

    private final String[] exposed;

    private final String group;

    private final String title;

    private final String description;

    private final String version;

}
