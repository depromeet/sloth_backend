package com.sloth.global.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("entity not found. id: " + id);
    }

}
