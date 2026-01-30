package com.company.back.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.back.controller.dto.CreateUserRequest;
import com.company.back.controller.dto.LoginRequest;
import com.company.back.controller.dto.LoginResponse;
import com.company.back.controller.dto.UserResponse;
import com.company.back.entity.User;
import com.company.back.security.JwtService;
import com.company.back.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final JwtService jwtService;

  public UserController(UserService userService, JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll() {
    List<User> users = userService.findaAll();
    List<UserResponse> response = users.stream()
        .map(UserResponse::from)
        .toList();

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(
      @RequestBody CreateUserRequest request
  ) {
    User user = userService.createUser(
        request.email(),
        request.password()
    );

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(UserResponse.from(user));
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
