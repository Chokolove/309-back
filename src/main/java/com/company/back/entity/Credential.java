package com.company.back.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.company.back.entity.enums.CredentialStatus;
import com.company.back.entity.enums.CredentialType;

@Entity
@Table(name = "credentials")
@EntityListeners(AuditingEntityListener.class)
public class Credential {

  @Id
  @GeneratedValue
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CredentialType type;

  @Column(nullable = false)
  private String issuer;

  @Column(name = "license_number", nullable = false)
  private String licenseNumber;

  @Column(name = "expiry_date")
  private LocalDate expiryDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CredentialStatus status = CredentialStatus.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  protected Credential() {}

  public Credential(
    User user,
    CredentialType type,
    String issuer,
    String licenseNumber,
    LocalDate expiryDate
) {
  this.user = user;
  this.type = type;
  this.issuer = issuer;
  this.licenseNumber = licenseNumber;
  this.expiryDate = expiryDate;
}

  public UUID getId() {
    return id;
  }

  public CredentialType getType() {
    return type;
  }

  public String getIssuer() {
    return issuer;
  }

  public String getLicenseNumber() {
    return licenseNumber;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public CredentialStatus getStatus() {
    return status;
  }

  public User getUser() {
    return user;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void approve() {
    this.status = CredentialStatus.APPROVED;
  }

  public void reject() {
    this.status = CredentialStatus.REJECTED;
  }

  public void expire() {
    this.status = CredentialStatus.EXPIRED;
  }

  public void softDelete() {
    this.deletedAt = Instant.now();
  }
}
