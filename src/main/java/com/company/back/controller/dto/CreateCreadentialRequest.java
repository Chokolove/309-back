package com.company.back.controller.dto;

import java.time.LocalDate;

import com.company.back.entity.enums.CredentialType;

public record CreateCreadentialRequest(
    CredentialType type,
    String issuer,
    String licenseNumber,
    LocalDate expiryDate) {

}
