package com.company.back.controller.dto;

import java.util.UUID;

public record AuthenticatedUser(UUID id, String email) {
}
