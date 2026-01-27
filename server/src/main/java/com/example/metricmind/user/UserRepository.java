package com.example.metricmind.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByGoogleUserId(String googleUserId);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByGoogleUserId(String googleUserId);
}