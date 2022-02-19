package com.javabackend.ecomercial.repository;
import java.util.Optional;

import com.javabackend.ecomercial.dto.ERole;
import com.javabackend.ecomercial.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	Optional<RoleEntity> findByName(ERole name);
	RoleEntity findByCode(String code);
}