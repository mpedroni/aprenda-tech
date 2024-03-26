package com.mpedroni.aprendatech.infra.courses.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, Long> {
    Optional<CourseJpaEntity> findByCode(String code);

    Page<CourseJpaEntity> findByStatus(String status, PageRequest request);
}
