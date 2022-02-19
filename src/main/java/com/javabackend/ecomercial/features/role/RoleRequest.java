package com.javabackend.ecomercial.features.role;


import com.javabackend.ecomercial.dto.ERole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {
    private ERole name;
    private String code;
}
