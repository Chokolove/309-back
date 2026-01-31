package com.company.back.controller.dto;

import java.time.LocalDate;

import com.company.back.entity.enums.CredentialType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCredentialRequest(

    @NotNull(message = "type is required") CredentialType type,

    @NotBlank(message = "issuer is required") String issuer,

    @NotBlank(message = "licenseNumber is required") String licenseNumber,

    @NotNull(message = "expiryDate is required") @Future(message = "expiryDate must be in the future") LocalDate expiryDate

) {
}
