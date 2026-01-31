package com.company.back.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.back.entity.Credential;
import com.company.back.entity.User;
import com.company.back.entity.enums.CredentialStatus;
import com.company.back.entity.enums.CredentialType;
import com.company.back.exception.CredentialNotFoundException;
import com.company.back.exception.UserNotFoundException;
import com.company.back.repository.CredentialRepository;
import com.company.back.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CredentialService {
  private final CredentialRepository credentialRepository;
  private final UserRepository userRepository;

  public CredentialService(CredentialRepository credentialRepository, UserRepository userRepository) {
    this.credentialRepository = credentialRepository;
    this.userRepository = userRepository;
  }

  public List<Credential> findByUser(UUID userId) {
    return credentialRepository.findByUserIdAndDeletedAtIsNull(userId);
  }

  public List<Credential> findByUserWithFilters(UUID userId, CredentialStatus status, CredentialType type,
      String cursor, int limit) {
    UUID cursorId = cursor != null ? UUID.fromString(cursor) : null;
    return credentialRepository.findByUserWithFilters(userId, status, type, cursorId, limit);
  }

  public Credential findById(UUID id, UUID userId) {
    return credentialRepository.findByIdAndUser_IdAndDeletedAtIsNull(id, userId)
        .orElseThrow(() -> new CredentialNotFoundException(id));
  }

  public Credential createCredential(
      UUID userId,
      CredentialType type,
      String issuer,
      String licenseNumber,
      LocalDate expiryDate) {
    User user = userRepository.findByIdAndDeletedAtIsNull(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    Credential credential = new Credential(
        user,
        type,
        issuer,
        licenseNumber,
        expiryDate);

    return credentialRepository.save(credential);
  }

  @Transactional
  public Credential updateStatus(UUID userId, UUID credentialId, CredentialStatus newStatus) {
    Credential credential = credentialRepository.findByIdAndUser_IdAndDeletedAtIsNull(credentialId, userId)
        .orElseThrow(() -> new CredentialNotFoundException(credentialId));
    switch (newStatus) {
      case APPROVED -> credential.approve();
      case REJECTED -> credential.reject();
      case EXPIRED -> throw new IllegalArgumentException("Cannot manually set status to EXPIRED");
      case PENDING -> throw new IllegalArgumentException("Cannot manually set status to PENDING");
    }
    return credential;
  }

  @Transactional
  public void deleteCredential(UUID userId, UUID credentialId) {
    Credential credential = credentialRepository.findByIdAndUser_IdAndDeletedAtIsNull(credentialId, userId)
        .orElseThrow(() -> new CredentialNotFoundException(credentialId));
    if (credential.getStatus() == CredentialStatus.PENDING || credential.getStatus() == CredentialStatus.REJECTED) {
      credential.softDelete();
    } else {
      throw new IllegalArgumentException("Cannot delete credential with status " + credential.getStatus());
    }
  }

}