package com.mpedroni.aprendatech.infra.config.security;

import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JpaUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userRepository;

    public JpaUserDetailsService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username \"%s\" not found.".formatted(username)));

        return User.withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}
