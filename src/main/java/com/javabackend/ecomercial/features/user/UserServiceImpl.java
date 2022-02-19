package com.javabackend.ecomercial.features.user;

import com.javabackend.ecomercial.common.conveter.Converter;
import com.javabackend.ecomercial.domain.entity.RoleEntity;
import com.javabackend.ecomercial.domain.entity.UserEntity;
import com.javabackend.ecomercial.dto.ERole;
import com.javabackend.ecomercial.exception.AppException;
import com.javabackend.ecomercial.exception.ErrorCode;
import com.javabackend.ecomercial.exception.ResourceNotFoundException;
import com.javabackend.ecomercial.payload.request.SignupRequest;
import com.javabackend.ecomercial.payload.response.MessageResponse;
import com.javabackend.ecomercial.repository.RoleRepository;
import com.javabackend.ecomercial.repository.UserRepository;
import com.javabackend.ecomercial.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    PasswordEncoder encoder;

    @Override
    @Transactional
    public UserResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new AppException(ErrorCode.ROLE_EXISTS);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        UserEntity userEntity = new UserEntity(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<RoleEntity> roleEntities = new HashSet<>();
        if (strRoles == null) {
            RoleEntity userRoleEntity = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
            roleEntities.add(userRoleEntity);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRoleEntity = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roleEntities.add(adminRoleEntity);
                        break;
                    case "mod":
                        RoleEntity modRoleEntity = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roleEntities.add(modRoleEntity);
                        break;
                    default:
                        RoleEntity userRoleEntity = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roleEntities.add(userRoleEntity);
                }
            });
        }
        userEntity.setRoleEntities(roleEntities);
        userEntity = userRepository.save(userEntity);
        return Converter.toModel(userEntity, UserResponse.class);
    }
}
