package com.company.back.controller.dto;

import com.company.back.entity.enums.CredentialStatus;

public record UpdateCredentialStatusRequest(CredentialStatus status) {

}
