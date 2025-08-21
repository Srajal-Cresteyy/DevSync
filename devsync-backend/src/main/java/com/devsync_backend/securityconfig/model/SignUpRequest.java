package com.devsync_backend.securityconfig.model;


import lombok.Data;

@Data
public class SignUpRequest {
    private String userName ;
    private String password ;
    private String[] userRole;
}
