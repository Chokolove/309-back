package com.company.back.controller.dto;

import java.util.UUID;

import com.company.back.entity.User;

public record LoginResponse(
    UUID id,
    String email
) {
  public static LoginResponse from(User user) {
    return new LoginResponse(user.getId(), user.getEmail());
  }
}