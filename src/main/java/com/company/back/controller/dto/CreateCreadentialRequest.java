package com.company.back.controller.dto;

import org.apache.logging.log4j.CloseableThreadContext.Instance;

import com.company.back.entity.enums.CredentialType;

public record CreateCreadentialRequest(
  CredentialType type,
  String issuer,
  String licenseNumber,
  Instance expiry_date
) {
  
}
