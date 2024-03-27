package com.mpedroni.aprendatech.infra.feedbacks.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Feedback")
@Table(name = "feedbacks")
public class FeedbackJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "course_id")
    Long courseId;

    @Column(name = "user_id")
    Long userId;

    Float rating;
    String reason;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Deprecated
    public FeedbackJpaEntity() {
    }

    public FeedbackJpaEntity(Long courseId, Long userId, Float rating, String reason) {
        this.courseId = courseId;
        this.userId = userId;
        this.rating = rating;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "FeedbackJpaEntity{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
