package com.sloth.app.entitymanager.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sloth.exception.BusinessException;
import com.sloth.exception.EntityNotFoundException;
import com.sloth.exception.EntityValidException;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.ToOne;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntityManageService {

    private final ApplicationContext applicationContext;
    private final Map<String, String> entityMap;

    @SuppressWarnings("all")
    public Page<Map<String, Object>> findAll(String entityName, int page, int size, String type, String keyword) {
        JpaRepository repository = this.getJpaRepository(entityName);
        Sort idBySort = Sort.by(Sort.Direction.DESC, entityName + "Id");
        Pageable pageable = PageRequest.of(page - 1, size, idBySort);

        Page result;
        if (type.isEmpty()) {
            result = repository.findAll(pageable);
        } else {
            try {
                Method method = repository.getClass().getDeclaredMethod("findBy" + type, String.class, Pageable.class);
                result = (Page) method.invoke(repository, keyword, pageable);
            } catch (NoSuchMethodException e) {
                throw new BusinessException("findBy" + type + " method not found");
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new BusinessException(keyword + " field not found.");
            }
        }

        Page<Map<String, Object>> map = (Page<Map<String, Object>>) result.map(new Function<Object, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(Object o) {
                return EntityMapper.entityToMap(o);
            }
        });
        return map;
    }


    @SuppressWarnings("all")
    public Map<String, Object> findById(String entityName, Long id) {
        try {
            JpaRepository jpaRepository = this.getJpaRepository(entityName);
            Object entity = jpaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id));

            return EntityMapper.entityToMap(entity);
        } catch (Throwable throwable) {
            throw new EntityNotFoundException(id);
        }
    }

    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity(String entityName, Long id, Map<String, Object> requestMap) throws Throwable {
        validateField(entityName, requestMap);
        JpaRepository repository = this.getJpaRepository(entityName);

        Object entityInstance = repository.findById(id)
                .orElseThrow(() -> new BusinessException("entity not found"));

        Set<String> keySet = requestMap.keySet();
        for (String key : keySet) {

            if(key == "id") {
                continue;
            }

            Object value = requestMap.get(key);
            System.out.println(Arrays.toString(entityInstance.getClass().getMethods()));
            System.out.println("set" + key.substring(0, 1).toUpperCase() + key.substring(1));

            Field field = entityInstance.getClass().getDeclaredField(key);
            Method setter = entityInstance.getClass().getMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), field.getType());

            if (field.getType().isEnum()) {
                setter.invoke(entityInstance, Enum.valueOf((Class<Enum>) field.getType(), requestMap.get(key).toString()));
            } else {
                setter.invoke(entityInstance, requestMap.get(key));
            }
        }
    }

    @SuppressWarnings("all")
    @Transactional
    public Object createEntity(String entityName, Map<String, Object> requestMap) {
        try {
            validateField(entityName, requestMap);
            JpaRepository repository = this.getJpaRepository(entityName);

            String entityFullPackageName = entityMap.get(entityName);
            Class<?> entityClass = Class.forName(entityFullPackageName);

            Constructor<?> constructor = entityClass.getConstructor();
            Object entityInstance = constructor.newInstance();

            Set<String> keySet = requestMap.keySet();
            for (String key: keySet) {
                Method setter = entityClass.getMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1));

                Field field = entityClass.getDeclaredField(key);

                field.setAccessible(true);
                if (field.getType().isEnum()) {
                    setter.invoke(entityInstance, Enum.valueOf((Class<Enum>) field.getType(), requestMap.get(key).toString()));
                } else {
                    setter.invoke(entityInstance, requestMap.get(key));
                }
                field.setAccessible(false);
            }

            Object savedEntity = repository.save(entityInstance);

            Field idField = entityClass.getDeclaredField("id");
            idField.setAccessible(true);
            return idField.get(savedEntity);
        } catch (ClassNotFoundException e) {
            throw new BusinessException("entity type not found");
        } catch (NoSuchFieldException e) {
            throw new BusinessException("field not found");
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("all")
    @Transactional
    public void deleteEntities(String entityName, String ids) {
        JpaRepository jpaRepository = this.getJpaRepository(entityName);

        String[] idStrArr = ids.split(",");
        try {
            for (String idStr : idStrArr) {
                long id = Long.parseLong(idStr.trim());
                jpaRepository.deleteById(id);
            }
        } catch (NumberFormatException e) {
            throw new EntityValidException(Arrays.asList("id"));
        }
    }

    public List<String> getFieldList(String entityName) {
        String entityFullName = entityMap.get(entityName);

        try {
            Class<?> clazz = Class.forName(entityFullName);

            List<String> fieldNames = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                if ( field.isAnnotationPresent(JsonIgnore.class) ||
                     field.isAnnotationPresent(OneToOne.class)   ||
                     field.isAnnotationPresent(OneToMany.class)  ||
                     field.isAnnotationPresent(ManyToOne.class)  ||
                     field.isAnnotationPresent(ManyToMany.class)
                ) {
                    continue;
                }

                fieldNames.add(field.getName());
            }
            return fieldNames;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("all")
    private JpaRepository getJpaRepository(String entityName) {
        JpaRepository repository = (JpaRepository) applicationContext.getBean(entityName + "Repository");

        if (repository == null) {
            throw new BusinessException("jpa repository not found");
        }

        return repository;
    }

    private void validateField(String entityName, Map<String, Object> requestMap) {
        try {
            String entityFullName = entityMap.get(entityName);
            Class<?> clazz = Class.forName(entityFullName);

            List<Field> fields = Stream.of(clazz.getDeclaredFields())
                    .filter((field) -> field.getDeclaredAnnotation(JsonIgnore.class) == null)
                    .filter((field) -> field.getDeclaredAnnotation(Id.class) == null)
                    .filter((field) -> field.getDeclaredAnnotation(OneToOne.class) == null)
                    .filter((field) -> field.getDeclaredAnnotation(ManyToOne.class) == null)
                    .filter((field) -> field.getDeclaredAnnotation(OneToMany.class) == null)
                    .filter((field) -> field.getDeclaredAnnotation(ManyToMany.class) == null)
                    .collect(Collectors.toList());

            Set<String> keySet = requestMap.keySet();
            List<String> emptyFieldList = new ArrayList<>();
            for (Field field : fields) {
                String fieldName = field.getName();

                if (!keySet.contains(fieldName)) {
                    emptyFieldList.add(fieldName);
                } else if(field.isAnnotationPresent(Email.class)) {
                    //todo 정규식으로 Email 검사 하기
                }

            }

            if (emptyFieldList.isEmpty()) {
                return;
            }

            throw new EntityValidException(emptyFieldList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
