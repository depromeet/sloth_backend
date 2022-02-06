package com.sloth.global.config.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.*;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * 엔티티 이름 리스트 빈 등록
     * @return 엔티티 이름 리스트
     */
    @Bean
    public Map<String, String> entityMap(){
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        Map<String , String> entityMap = new HashMap<>();

        for (EntityType<?> entity : entities) {
            String packageName = entity.getJavaType().getPackageName();
            String entityName = entity.getName();
            String firstLowerEntityName = entityName.substring(0,1).toLowerCase() + entityName.substring(1);
            entityMap.put(firstLowerEntityName, packageName + "." + entityName);
        }

        return entityMap;
    }

}
