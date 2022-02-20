package com.javabackend.ecomercial.features.user;

import com.javabackend.ecomercial.common.conveter.Converter;
import com.javabackend.ecomercial.domain.entity.RefreshToken;
import com.javabackend.ecomercial.domain.entity.RoleEntity;
import com.javabackend.ecomercial.domain.entity.UserEntity;
import com.javabackend.ecomercial.dto.ERole;
import com.javabackend.ecomercial.exception.AppException;
import com.javabackend.ecomercial.exception.ErrorCode;
import com.javabackend.ecomercial.exception.ResourceNotFoundException;
import com.javabackend.ecomercial.payload.request.LoginRequest;
import com.javabackend.ecomercial.payload.request.SignupRequest;
import com.javabackend.ecomercial.payload.response.JwtResponse;
import com.javabackend.ecomercial.payload.response.MessageResponse;
import com.javabackend.ecomercial.repository.RoleRepository;
import com.javabackend.ecomercial.repository.UserRepository;
import com.javabackend.ecomercial.security.jwt.JwtUtils;
import com.javabackend.ecomercial.security.services.RefreshTokenService;
import com.javabackend.ecomercial.security.services.UserDetailsImpl;
import com.javabackend.ecomercial.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

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

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles);
    }
}
