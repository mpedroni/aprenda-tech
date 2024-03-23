package com.mpedroni.aprendatech.infra.users.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "User")
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String username;
    private String email;
    private String password;
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Deprecated
    public UserJpaEntity() {}

    private UserJpaEntity(
        Long id,
        String name,
        String username,
        String email,
        String password,
        String role,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UserJpaEntity(
            String name,
            String username,
            String email,
            String password,
            String role
    ) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    static public UserJpaEntity from(
        Long id,
        String name,
        String username,
        String email,
        String password,
        String role
    ) {
        return new UserJpaEntity(id, name, username, email, password, role, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "UserJpaEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
