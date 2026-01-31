package com.company.back.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;

import com.company.back.config.JpaConfig;
import com.company.back.entity.Credential;
import com.company.back.entity.User;
import com.company.back.entity.enums.CredentialType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@EntityScan(basePackages = "com.company.back.entity")
class CredentialRepositoryTest {

  @Autowired
  private CredentialRepository credentialRepository;

  @Autowired
  private UserRepository userRepository;

  private static final int SECONDS_IN_DAY = 24 * 3600;
  private Instant now;
  private Instant graceThreshold;

  @BeforeEach
  void setUp() {
    now = Instant.now();
    graceThreshold = now.minusSeconds(14 * SECONDS_IN_DAY);
    credentialRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Nested
  @DisplayName("Scenario 1: No other credentials of that type exist")
  class NoOtherCredentials {
    @Test
    void expiresCredential() {
      User user = new User("user1@test.com", "hash");
      userRepository.save(user);

      Credential c = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA111",
          LocalDate.now());
      c.approve();
      credentialRepository.save(c);

      int updated = credentialRepository.expireEligibleCredentials(LocalDate.now(), graceThreshold);
      assertThat(updated).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("Scenario 2: PENDING renewal submitted 5 days ago")
  class PendingRenewal {
    @Test
    void doesNotExpireOriginalCredential() {
      User user = new User("user2@test.com", "hash");
      userRepository.save(user);

      Credential original = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA222",
          LocalDate.now());
      original.approve();
      credentialRepository.save(original);

      Credential pending = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA223",
          LocalDate.now().plusDays(30));
      pending.pending();
      pending.setCreatedAt(now.minusSeconds(5 * SECONDS_IN_DAY));
      credentialRepository.save(pending);

      int updated = credentialRepository.expireEligibleCredentials(LocalDate.now(), graceThreshold);
      assertThat(updated).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("Scenario 3: REJECTED renewal submitted yesterday")
  class RejectedRenewal {
    @Test
    void expiresOriginalCredential() {
      User user = new User("user3@test.com", "hash");
      userRepository.save(user);

      Credential original = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA333",
          LocalDate.now());
      original.approve();
      credentialRepository.save(original);

      Credential rejected = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA334",
          LocalDate.now().plusDays(30));
      rejected.reject();
      rejected.setCreatedAt(now.minusSeconds(SECONDS_IN_DAY));
      credentialRepository.save(rejected);

      int updated = credentialRepository.expireEligibleCredentials(LocalDate.now(), graceThreshold);
      assertThat(updated).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("Scenario 4: Grace period, pending renewal gets REJECTED")
  class GracePeriodRejected {
    @Test
    void expiresOriginalCredential() {
      User user = new User("user4@test.com", "hash");
      userRepository.save(user);

      Credential original = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA444",
          LocalDate.now().minusDays(10));
      original.approve();
      credentialRepository.save(original);

      Credential rejected = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA445",
          LocalDate.now().plusDays(20));
      rejected.reject();
      rejected.setCreatedAt(now.minusSeconds(2 * SECONDS_IN_DAY));
      credentialRepository.save(rejected);

      int updated = credentialRepository.expireEligibleCredentials(LocalDate.now(), graceThreshold);
      assertThat(updated).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("Scenario 5: Grace period, pending renewal gets APPROVED")
  class GracePeriodApproved {
    @Test
    void expiresOriginalCredential() {
      User user = new User("user5@test.com", "hash");
      userRepository.save(user);

      Credential original = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA555",
          LocalDate.now().minusDays(10));
      original.approve();
      credentialRepository.save(original);

      Credential approved = new Credential(user, CredentialType.HVAC_LICENSE, "Agency", "AAA556",
          LocalDate.now().plusDays(30));
      approved.approve();
      approved.setCreatedAt(now.minusSeconds(2 * SECONDS_IN_DAY));
      credentialRepository.save(approved);

      int updated = credentialRepository.expireEligibleCredentials(LocalDate.now(), graceThreshold);
      assertThat(updated).isEqualTo(1);
    }
  }
}
