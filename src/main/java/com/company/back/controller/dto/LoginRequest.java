package com.company.back.controller.dto;

public record LoginRequest (
  String email,
  String password
) {}
