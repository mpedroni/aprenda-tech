package com.mpedroni.aprendatech.infra.courses.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseJpaRepository extends CrudRepository<CourseJpaEntity, Long> {
    Optional<CourseJpaEntity> findByCode(String code);
}
