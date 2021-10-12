package com.sloth.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity implements EntityCommonMethod {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    public List<Object> getAllValue() throws Exception {
        Field[] fields = this.getClass().getDeclaredFields();
        List<Object> objects = new ArrayList<>();

        for (Field field : fields) {
            if(field.getAnnotation(JsonIgnore.class) != null){
                continue;
            }
            field.setAccessible(true);
            Object obj = field.get(this);
            objects.add(obj);
        }

        return objects;
    }

}
