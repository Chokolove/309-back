package com.company.back.controller.dto;

public record CreateUserRequest(
    String email,
    String password
) {}
