package com.mpedroni.aprendatech.infra.users.api;

import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest request) {
        var user = createUserUseCase.execute(
                request.name(),
                request.username(),
                request.email(),
                request.password(),
                request.role()
        );

        URI location = URI.create("/users/" + user.getId());

        return ResponseEntity.created(location).build();
    }
}
