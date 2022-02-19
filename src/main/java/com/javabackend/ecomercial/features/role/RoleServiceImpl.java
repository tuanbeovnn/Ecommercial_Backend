package com.javabackend.ecomercial.features.role;

import com.javabackend.ecomercial.common.conveter.Converter;
import com.javabackend.ecomercial.domain.entity.RoleEntity;
import com.javabackend.ecomercial.exception.AppException;
import com.javabackend.ecomercial.exception.ErrorCode;
import com.javabackend.ecomercial.repository.RoleRepository;
import com.javabackend.ecomercial.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        RoleEntity roleEntity = roleRepository.findByCode(roleRequest.getCode());
        if (null != roleEntity) {
            throw new AppException(ErrorCode.ROLE_EXISTS);
        }
        roleEntity = Converter.toModel(roleRequest, RoleEntity.class);
        roleEntity = roleRepository.save(roleEntity);
        return Converter.toModel(roleEntity, RoleResponse.class);
    }
}
