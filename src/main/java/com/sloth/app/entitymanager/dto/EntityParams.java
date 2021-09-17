package com.sloth.app.entitymanager.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityParams {
    private int page;
    private int size;
    private String type;
    private String keyword;

    public EntityParams() {
        this.page = 1;
        this.size = 10;
        this.type = "";
        this.keyword = "";
    }
}
