package com.example.demo.payload.response;

import com.example.demo.model.Role;

import java.util.Set;

public class SignUpResponse {
//    private String token;
    private String username;
    private Set<Role> roles;

    public SignUpResponse(String username, Set<Role> roles) {
//        this.token = token;
        this.username = username;
        this.roles = roles;
    }

}
