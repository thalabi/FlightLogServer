package com.kerneldc.flightlogserver.security.bean;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CopyUserRequest {

    @NotBlank
    private String fromUsername;

    @NotBlank
    private String toUsername;
}
