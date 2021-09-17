package com.sloth.app.entitymanager.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityMapper {

    public static Map<String, Object> entityToMap(Object entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();

        Map<String, Object> map = new LinkedHashMap<>();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            boolean isJsonIgnoreField = field.isAnnotationPresent(JsonIgnore.class);
            if (isJsonIgnoreField) {
                continue;
            }
            try {
                Object value = field.get(entity);
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }

        return map;
    }

}
