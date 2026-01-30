package com.company.back.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.company.back.entity.Credential;
import com.company.back.entity.enums.CredentialStatus;
import com.company.back.entity.enums.CredentialType;

public record CredentialResponse(
    UUID id,
    CredentialType type,
    String issuer,
    String licenseNumber,
    LocalDate expiryDate,
    CredentialStatus status
) {
    public static CredentialResponse from(Credential credential) {
        return new CredentialResponse(
            credential.getId(),
            credential.getType(),
            credential.getIssuer(),
            credential.getLicenseNumber(),
            credential.getExpiryDate(),
            credential.getStatus()
        );
    }
}
