package qreol.project.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.mappers.UserMapper;
import qreol.project.tasklist.service.AuthService;
import qreol.project.tasklist.service.UserService;
import qreol.project.tasklist.web.dto.auth.JwtRequest;
import qreol.project.tasklist.web.dto.auth.JwtResponse;
import qreol.project.tasklist.web.dto.user.UserDTO;
import qreol.project.tasklist.web.validation.flags.OnCreate;
import qreol.project.tasklist.web.validation.validators.UserValidator;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Login")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<JwtResponse> login(@Validated @RequestBody JwtRequest loginRequest) {
        return new HttpEntity<>(authService.login(loginRequest));
    }

    @PostMapping("/registration")
    @Operation(summary = "Register")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<UserDTO> register(@Validated(OnCreate.class) @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);

        User createdUser = userService.create(userMapper.toEntity(userDTO));

        return new HttpEntity<>(userMapper.toDTO(createdUser));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token")
    public HttpEntity<JwtResponse> refresh(@RequestBody String refreshToken) {
        return new HttpEntity<>(authService.refresh(refreshToken));
    }

}
