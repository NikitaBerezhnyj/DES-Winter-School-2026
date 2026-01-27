package com.example.metricmind.session;

import com.example.metricmind.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
   
    @Query("SELECT s FROM Session s WHERE s.sessionToken = :token AND s.isActive = true AND s.expiresAt > :now")
    Optional<Session> findActiveByToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    @Query("SELECT s FROM Session s WHERE s.user = :user AND s.isActive = true AND s.expiresAt > :now")
    List<Session> findActiveSessionsByUser(@Param("user") User user, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("UPDATE Session s SET s.isActive = false WHERE s.user = :user")
    void deactivateAllUserSessions(@Param("user") User user);
    
    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < :now")
    void deleteExpiredSessions(@Param("now") LocalDateTime now);
}