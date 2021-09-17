package com.sloth.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityValidException extends RuntimeException {

    private final List<String> entities;

    public EntityValidException(List<String> entities) {
        super("entity valid");
        this.entities = entities;
    }

}
