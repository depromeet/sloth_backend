package com.sloth.api.health.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseHealthCheckDto {

    private boolean status;
    private String health;

}
