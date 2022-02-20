package com.javabackend.ecomercial.service;

import com.javabackend.ecomercial.features.user.UserResponse;
import com.javabackend.ecomercial.payload.request.LoginRequest;
import com.javabackend.ecomercial.payload.request.SignupRequest;
import com.javabackend.ecomercial.payload.response.JwtResponse;

public interface UserService {
    UserResponse registerUser(SignupRequest signupRequest);
    JwtResponse signIn(LoginRequest loginRequest);
}
