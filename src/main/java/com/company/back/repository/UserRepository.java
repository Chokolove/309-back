package com.company.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.back.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

  List<User> findByDeletedAtIsNull();
  Optional<User> findByIdAndDeletedAtIsNull(UUID id);
  Optional<User> findByEmailAndDeletedAtIsNull(String email);

  
} 
