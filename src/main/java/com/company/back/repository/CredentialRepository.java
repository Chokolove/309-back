package com.company.back.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.back.entity.Credential;
import com.company.back.entity.enums.CredentialStatus;
import com.company.back.entity.enums.CredentialType;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {

  List<Credential> findByUserIdAndDeletedAtIsNull(UUID userId);

  Optional<Credential> findByIdAndUser_IdAndDeletedAtIsNull(UUID id, UUID userId);

  List<Credential> findByStatusAndExpiryDateBeforeAndDeletedAtIsNull(CredentialStatus status, LocalDateTime dateTime);

  @Query("""
          SELECT c FROM Credential c
          WHERE c.user.id = :userId
            AND (:status IS NULL OR c.status = :status)
            AND (:type IS NULL OR c.type = :type)
            AND (:cursor IS NULL OR c.id > :cursor)
            AND c.deletedAt IS NULL
          ORDER BY c.id ASC
      """)
  List<Credential> findByUserWithFilters(
      @Param("userId") UUID userId,
      @Param("status") CredentialStatus status,
      @Param("type") CredentialType type,
      @Param("cursor") UUID cursor,
      @Param("limit") int limit);

  @Modifying
  @Query("""
        UPDATE Credential c
        SET c.status = 'EXPIRED'
        WHERE c.status = 'APPROVED'
          AND c.expiryDate <= :now
          AND c.deletedAt IS NULL
          AND NOT EXISTS (
            SELECT 1 FROM Credential newer
            WHERE newer.user = c.user
              AND newer.type = c.type
              AND newer.status = 'PENDING'
              AND newer.deletedAt IS NULL
              AND newer.createdAt >= :graceThreshold
          )
      """)
  int expireEligibleCredentials(
      @Param("now") LocalDate now,
      @Param("graceThreshold") Instant graceThreshold);
}
