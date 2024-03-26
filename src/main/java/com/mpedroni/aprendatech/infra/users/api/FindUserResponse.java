package com.mpedroni.aprendatech.infra.users.api;

import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;

public record FindUserResponse(
        Long id,
        String name,
        String email,
        String role
) {
    static FindUserResponse from(UserJpaEntity user) {
        return new FindUserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
