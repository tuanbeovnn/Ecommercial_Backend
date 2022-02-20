package com.javabackend.ecomercial.features.user;

import com.javabackend.ecomercial.domain.entity.RefreshToken;
import com.javabackend.ecomercial.exception.TokenRefreshException;
import com.javabackend.ecomercial.payload.request.LogOutRequest;
import com.javabackend.ecomercial.payload.request.LoginRequest;
import com.javabackend.ecomercial.payload.request.SignupRequest;
import com.javabackend.ecomercial.payload.request.TokenRefreshRequest;
import com.javabackend.ecomercial.payload.response.JwtResponse;
import com.javabackend.ecomercial.payload.response.MessageResponse;
import com.javabackend.ecomercial.payload.response.TokenRefreshResponse;
import com.javabackend.ecomercial.security.jwt.JwtUtils;
import com.javabackend.ecomercial.security.services.RefreshTokenService;
import com.javabackend.ecomercial.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserApi {
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.signIn(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        UserResponse userResponse = userService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
