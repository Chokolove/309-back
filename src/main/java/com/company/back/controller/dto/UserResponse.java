package com.company.back.controller.dto;

import com.company.back.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String email,
    Instant createdAt,
    Instant updatedAt
) {
  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt()
    );
  }
}
