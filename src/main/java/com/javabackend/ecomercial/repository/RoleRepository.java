package com.javabackend.ecomercial.repository;
import java.util.Optional;

import com.javabackend.ecomercial.dto.ERole;
import com.javabackend.ecomercial.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}