package com.javabackend.ecomercial.domain.entity;

import com.javabackend.ecomercial.domain.base.BaseEntity;
import com.javabackend.ecomercial.dto.ERole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
@Getter
@Setter
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @Column(length = 20)
    private String code;
}