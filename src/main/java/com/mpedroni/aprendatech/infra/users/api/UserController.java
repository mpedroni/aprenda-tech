package com.mpedroni.aprendatech.infra.users.api;

import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserJpaRepository repository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request) {
        var user = new UserJpaEntity(
                request.name(),
                request.username(),
                request.email(),
                request.password(),
                request.role()
        );

        repository.save(user);

        URI location = URI.create("/users/" + user.getId());

        return ResponseEntity.created(location).build();
    }
}
