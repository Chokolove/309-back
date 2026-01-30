package com.company.back.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import com.company.back.controller.dto.CredentialResponse;
import com.company.back.entity.Credential;
import com.company.back.service.CredentialService;

@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialController {
  private final CredentialService credentialService;
  public CredentialController(CredentialService credentialService) {
    this.credentialService = credentialService;
  }

  
}