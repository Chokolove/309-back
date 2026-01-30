package com.company.back.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.back.entity.Credential;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {

  List<Credential> findByUserIdAndDeletedAtIsNull(UUID userId);
  Optional<Credential> findByIdAndfindByUserIdAndDeletedAtIsNull(UUID id, UUID userId);
  
} 
