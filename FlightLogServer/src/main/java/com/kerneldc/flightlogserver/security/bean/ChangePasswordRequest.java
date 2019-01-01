package com.kerneldc.flightlogserver.security.bean;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
