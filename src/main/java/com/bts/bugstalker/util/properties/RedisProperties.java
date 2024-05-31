package com.bts.bugstalker.util.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisProperties {

    private int redisPort;

    private String redisHost;

    private String redisPassword;
}