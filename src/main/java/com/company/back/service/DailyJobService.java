package com.company.back.service;

import java.time.Instant;
import java.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.company.back.repository.CredentialRepository;

import jakarta.transaction.Transactional;

@Service
public class DailyJobService {

  private final CredentialRepository credentialRepository;

  public DailyJobService(CredentialRepository credentialRepository) {
    this.credentialRepository = credentialRepository;
  }

  @Scheduled(cron = "0 0 2 * * *", zone = "America/Lima")
  @Transactional
  public void expireCredentialsDaily() {
    LocalDate now = LocalDate.now();
    Instant graceThreshold = Instant.now().minusSeconds(14 * 24 * 3600);

    int expired = credentialRepository.expireEligibleCredentials(
        now,
        graceThreshold);

    System.out.println("Expired credentials count: " + expired);
  }
}
