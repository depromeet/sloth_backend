package com.sloth.app.entitymanager.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityMapper {

    public static Map<String, Object> entityToMap(Object entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();

        Map<String, Object> map = new LinkedHashMap<>();

        for (Field field : declaredFields) {
            field.setAccessible(true);

            boolean isIgnoreField = false;

            if ( field.isAnnotationPresent(JsonIgnore.class) ||
                 field.isAnnotationPresent(OneToOne.class)   ||
                 field.isAnnotationPresent(OneToMany.class)  ||
                 field.isAnnotationPresent(ManyToOne.class)  ||
                 field.isAnnotationPresent(ManyToMany.class)
            ) {
                isIgnoreField = true;
            }

            if (isIgnoreField) {
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
