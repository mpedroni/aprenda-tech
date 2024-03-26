package com.mpedroni.aprendatech.infra.users.api;

import com.mpedroni.aprendatech.domain.users.usecases.CreateUserCommand;
import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.domain.users.usecases.FindUserByUsernameUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    public UserController(CreateUserUseCase createUserUseCase, FindUserByUsernameUseCase findUserByUsernameUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserByUsernameUseCase = findUserByUsernameUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest request) {
        var aCommand = CreateUserCommand.with(
                request.name(),
                request.username(),
                request.email(),
                request.password(),
                request.role()
        );
        var user = createUserUseCase.execute(aCommand);

        URI location = URI.create("/users/" + user.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<FindUserResponse> find(@PathVariable String username) {
        var user = findUserByUsernameUseCase.execute(username);

        return ResponseEntity.ok(FindUserResponse.from(user));
    }
}
