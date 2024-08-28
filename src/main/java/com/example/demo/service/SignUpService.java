package com.example.demo.service;

import com.example.demo.payload.request.SignUpRequest;
import org.springframework.stereotype.Service;


public interface SignUpService {
    public void saveUser(SignUpRequest signUpRequest);
}
