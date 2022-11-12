package com.muizz.sajooq.configuration;

import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:sajooq-application.yaml", factory = YamlPropertySourceFactory.class)
public class SajooqConfig {
    
}
