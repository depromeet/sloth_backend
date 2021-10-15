package com.sloth.api.site.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SiteNameDto {

    private Long id;
    private String name;
}
