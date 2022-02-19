package com.javabackend.ecomercial.repository;
import com.javabackend.ecomercial.domain.entity.UserEntity;
import com.javabackend.ecomercial.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUser(UserEntity userEntity);
}