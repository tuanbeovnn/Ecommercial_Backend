package com.javabackend.ecomercial.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @Size(min = 3, max = 20)
    @NotEmpty(message = "Please provide a name")
    private String username;

    @NotEmpty(message = "Please provide a email")
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotNull(message = "Required")
    @Size(min = 6, max = 40)
    private String password;
}
