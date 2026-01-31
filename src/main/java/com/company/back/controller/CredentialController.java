package com.company.back.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.company.back.controller.dto.AuthenticatedUser;
import com.company.back.controller.dto.CreateCredentialRequest;
import com.company.back.controller.dto.CredentialResponse;
import com.company.back.controller.dto.UpdateCredentialStatusRequest;
import com.company.back.entity.Credential;
import com.company.back.entity.User;
import com.company.back.entity.enums.CredentialStatus;
import com.company.back.entity.enums.CredentialType;
import com.company.back.service.CredentialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialController {
  private final CredentialService credentialService;

  public CredentialController(CredentialService credentialService) {
    this.credentialService = credentialService;
  }

  @GetMapping
  public ResponseEntity<CredentialPage> findAll(
      @AuthenticationPrincipal User user,
      @RequestParam(required = false) CredentialStatus status,
      @RequestParam(required = false) CredentialType type,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "20") int limit) {
    List<Credential> credentials = credentialService.findByUserWithFilters(
        user.getId(), status, type, cursor, limit);

    List<CredentialResponse> items = credentials.stream()
        .map(CredentialResponse::from)
        .toList();

    String nextCursor = items.isEmpty() ? null : items.get(items.size() - 1).id().toString();

    return ResponseEntity.ok(new CredentialPage(items, nextCursor));
  }

  public record CredentialPage(List<CredentialResponse> items, String nextCursor) {
  }

  @GetMapping("/{credentialId}")
  public ResponseEntity<CredentialResponse> findById(
      @AuthenticationPrincipal User user,
      @PathVariable UUID credentialId) {
    Credential credential = credentialService.findById(credentialId, user.getId());
    if (credential == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(CredentialResponse.from(credential));
  }

  @PostMapping
  public ResponseEntity<CredentialResponse> create(
      @AuthenticationPrincipal AuthenticatedUser user,
      @Valid @RequestBody CreateCredentialRequest request) {
    Credential credential = credentialService.createCredential(
        user.id(),
        request.type(),
        request.issuer(),
        request.licenseNumber(),
        request.expiryDate());
    return ResponseEntity.ok(CredentialResponse.from(credential));
  }

  @DeleteMapping("/{credentialId}")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal User user,
      @PathVariable UUID credentialId) {
    credentialService.deleteCredential(user.getId(), credentialId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{credentialId}/status")
  public ResponseEntity<CredentialResponse> updateStatus(
      @AuthenticationPrincipal User user,
      @PathVariable UUID credentialId,
      @RequestBody UpdateCredentialStatusRequest request) {
    Credential credential = credentialService.updateStatus(user.getId(), credentialId, request.status());
    return ResponseEntity.ok(CredentialResponse.from(credential));
  }

}