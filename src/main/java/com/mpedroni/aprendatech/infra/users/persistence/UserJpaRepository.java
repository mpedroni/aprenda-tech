package com.mpedroni.aprendatech.infra.users.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends CrudRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByUsername(String username);
    Optional<UserJpaEntity> findByEmail(String email);
}
