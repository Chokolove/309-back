package com.company.back.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.back.entity.User;
import com.company.back.repository.UserRepository;

@Service
public class UserService {
  
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> findaAll() {
    return userRepository.findByDeletedAtIsNull();
  }

  public User findById(UUID id) {
    return userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }
  
  public User createUser(String email, String rawPassword){
    String hash = passwordEncoder.encode(rawPassword);
    User user = new User(email, hash);
    return userRepository.save(user);
  }

  public boolean checkPassword(User user, String rawPassword) {
    return passwordEncoder.matches(rawPassword, user.getPasswordHash());
  }
}
