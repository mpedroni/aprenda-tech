package com.mpedroni.aprendatech.infra.courses.persistence;

import org.springframework.data.repository.CrudRepository;

public interface CourseJpaRepository extends CrudRepository<CourseJpaEntity, Long> {
}
