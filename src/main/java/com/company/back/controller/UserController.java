package com.company.back.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.back.controller.dto.UserResponse;
import com.company.back.entity.User;
import com.company.back.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll() {
    List<User> users = userService.findaAll();
    List<UserResponse> response = users.stream()
        .map(UserResponse::from)
        .toList();

    return ResponseEntity.ok(response);
  }

}
