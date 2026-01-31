package com.company.back.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.back.entity.Credential;
import com.company.back.entity.User;
import com.company.back.entity.enums.CredentialStatus;
import com.company.back.entity.enums.CredentialType;
import com.company.back.repository.CredentialRepository;
import com.company.back.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

  @Mock
  private CredentialRepository credentialRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CredentialService credentialService;

  @Test
  void throwsExceptionWhenDeletingApprovedCredential() {
    UUID userId = UUID.randomUUID();
    UUID credentialId = UUID.randomUUID();

    User user = new User("test@test.com", "hash");

    Credential credential = new Credential(
        user,
        CredentialType.STATE_LICENSE,
        "DMV",
        "ABC123",
        LocalDate.now().plusYears(1));

    credential.approve();

    Mockito.when(
        credentialRepository.findByIdAndUser_IdAndDeletedAtIsNull(
            credentialId,
            userId))
        .thenReturn(Optional.of(credential));

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> credentialService.deleteCredential(userId, credentialId));

    assert (exception.getMessage().contains("APPROVED"));
  }

  @Test
  void deletesCredentialWhenStatusIsPending() {
    UUID userId = UUID.randomUUID();
    UUID credentialId = UUID.randomUUID();

    Credential credential = Mockito.mock(Credential.class);

    Mockito.when(credential.getStatus())
        .thenReturn(CredentialStatus.PENDING);

    Mockito.when(
        credentialRepository.findByIdAndUser_IdAndDeletedAtIsNull(credentialId, userId))
        .thenReturn(Optional.of(credential));

    credentialService.deleteCredential(userId, credentialId);

    Mockito.verify(credential).softDelete();
  }
}
