package com.io.hhplus.concert.common.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ActiveProfiles("test")
public class DataBaseCleanUp {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void execute() {
        List<String> tableNames = getTableNames();
        entityManager.flush();

        // mySQL foreign Key check 설정 변경
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        tableNames.forEach(tableName -> {
            // 테이블 데이터 초기화
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            // 시퀀스 초기화
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1").executeUpdate();
        });

        // mySQL foreign Key check 설정 원복
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    private List<String> getTableNames() {
        return entityManager.getMetamodel().getEntities().stream()
                .map(entityType -> {
                    Class<?> javaType = entityType.getJavaType();
                    Table tableAnnotation = javaType.getAnnotation(Table.class);
                    String tableName = (tableAnnotation != null && !tableAnnotation.name().isEmpty())
                            ? tableAnnotation.name()
                            : entityType.getName();
                    return tableName.toLowerCase(); // MySQL의 테이블 이름 대소문자 구분 문제 해결
                })
                .collect(Collectors.toList());
    }
}
