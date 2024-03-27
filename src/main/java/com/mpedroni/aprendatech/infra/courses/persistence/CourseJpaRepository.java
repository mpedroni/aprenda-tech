package com.mpedroni.aprendatech.infra.courses.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, Long> {
    Optional<CourseJpaEntity> findByCode(String code);

    Page<CourseJpaEntity> findByStatus(String status, PageRequest request);

    @Query("""
        SELECT NEW com.mpedroni.aprendatech.infra.courses.persistence.CourseNpsMetrics(
           c.id,
           c.code,
           (SELECT CAST(COUNT(f) AS int) FROM Feedback f WHERE f.courseId = c.id AND f.rating >= 9),
           (SELECT CAST(COUNT(f) AS int) FROM Feedback f WHERE f.courseId = c.id AND f.rating <= 6),
           (SELECT CAST(COUNT(f) AS int) FROM Feedback f WHERE f.courseId = c.id AND f.rating BETWEEN 7 AND 8)
        )
        FROM
            Course c
        WHERE (
            SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = c.id
        ) > 4
    """)
    Page<CourseNpsMetrics> findNpsByCourses(Pageable pageable);
}
