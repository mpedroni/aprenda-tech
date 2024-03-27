package com.mpedroni.aprendatech.infra.feedbacks.persistence;

import org.springframework.data.repository.CrudRepository;

public interface FeedbackJpaRepository extends CrudRepository<FeedbackJpaEntity, Long> {
}
