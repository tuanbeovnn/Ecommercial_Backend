package com.javabackend.ecomercial.service;

import com.javabackend.ecomercial.features.role.RoleRequest;
import com.javabackend.ecomercial.features.role.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
}
