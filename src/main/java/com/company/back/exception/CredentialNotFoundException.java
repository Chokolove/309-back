package com.company.back.exception;

import java.util.UUID;

public class CredentialNotFoundException extends RuntimeException {
  public CredentialNotFoundException(UUID id) {
    super("Credential not found: " + id);
  }
}
