package com.mpedroni.aprendatech.infra.courses.persistence;

import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Course")
@Table(name = "courses")
public class CourseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String code;
    String description;
    String status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "inactivated_at")
    LocalDateTime inactivatedAt;

    @Column(name = "instructor_id")
    Long instructorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id", insertable = false, updatable = false)
    UserJpaEntity instructor;

    public CourseJpaEntity() {
    }

    public CourseJpaEntity(String name, String code, String description, Long instructorId) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.instructorId = instructorId;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    public void inactivate() {
        this.status = "INACTIVE";
        this.inactivatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getInactivatedAt() {
        return inactivatedAt;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public UserJpaEntity getInstructor() {
        return instructor;
    }

    public boolean isActive() {
        return status.equals("ACTIVE");
    }

    @Override
    public String toString() {
        return "CourseJpaEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", inactivatedAt=" + inactivatedAt +
                ", instructorId=" + instructorId +
                '}';
    }
}
