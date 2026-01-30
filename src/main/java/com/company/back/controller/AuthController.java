package com.company.back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.back.controller.dto.CreateUserRequest;
import com.company.back.controller.dto.LoginRequest;
import com.company.back.controller.dto.LoginResponse;
import com.company.back.entity.User;
import com.company.back.security.JwtService;
import com.company.back.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;

  public AuthController(UserService userService, JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @Valid @RequestBody CreateUserRequest request
  ) {
    userService.createUser(
        request.email(),
        request.password()
    );

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest request
  ) {
    User user = userService.login(
        request.email(),
        request.password()
    );

    String token = jwtService.generateToken(user);

    return ResponseEntity.ok(new LoginResponse(token));
  }
}
