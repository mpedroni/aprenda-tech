package com.mpedroni.aprendatech.infra.enrollments.persistence;

import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Enrollment")
@Table(name = "enrollments")
public class EnrollmentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserJpaEntity user;

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private CourseJpaEntity course;

    @Deprecated
    public EnrollmentJpaEntity() {
    }

    public EnrollmentJpaEntity(Long courseId, Long userId) {
        this.courseId = courseId;
        this.userId = userId;
        this.enrolledAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public CourseJpaEntity getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return "EnrollmentJpaEntity{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", userId=" + userId +
                ", enrolledAt=" + enrolledAt +
                '}';
    }
}
