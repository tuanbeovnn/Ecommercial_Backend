package com.javabackend.ecomercial.features.role;

import com.javabackend.ecomercial.payload.response.MessageResponse;
import com.javabackend.ecomercial.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class RoleApi {
    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/role/create-role")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse response = roleService.createRole(roleRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
