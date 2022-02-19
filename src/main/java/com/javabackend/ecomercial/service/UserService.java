package com.javabackend.ecomercial.service;

import com.javabackend.ecomercial.features.user.UserResponse;
import com.javabackend.ecomercial.payload.request.SignupRequest;

public interface UserService {
    UserResponse registerUser(SignupRequest signupRequest);
}
