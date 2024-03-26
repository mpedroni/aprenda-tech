package com.mpedroni.aprendatech.infra.enrollments.persistence;

import org.springframework.data.repository.CrudRepository;

public interface EnrollmentJpaRepository extends CrudRepository<EnrollmentJpaEntity, Long> {
}
