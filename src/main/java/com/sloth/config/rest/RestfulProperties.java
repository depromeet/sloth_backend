package com.sloth.config.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("restemplate")
public class RestfulProperties {

    private int readTimeout;

    private int connectTimeout;

    private int maxConnTotal;

    private int maxConnPerRoute;

}
