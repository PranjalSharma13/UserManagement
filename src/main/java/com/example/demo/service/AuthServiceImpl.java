package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.RefreshTokenRequest;
import com.example.demo.payload.response.LoginResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication result;
        try {
            result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            //This creates an authentication token using the user's email (or username) and password
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials provided.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(result.getName());
//   The userDetailsService is typically an interface that loads user-specific data (like roles and permissions) from a database or another source.
        String accessToken = jwtUtil.generateToken(userDetails);
        //This line generates an access token using the user details. The accessToken is usually short-lived and is used to authenticate the user for subsequent requests.
        String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getEmail()); String username = userDetails.getUsername(); // Modify based on your UserDetails implementation
     // This generates a refresh token, which is typically long-lived and can be used to obtain a new access token without requiring the user to log in again. In this case, the email is used to generate the refresh token
        Set<Role> roles = userDetails.getAuthorities().stream()
                .map(authority -> {
                    RoleName roleName = RoleName.valueOf(authority.getAuthority());
                    return new Role(roleName);
                })
                .collect(Collectors.toSet());

        return new LoginResponse(username,roles,accessToken, refreshToken);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        boolean isRefreshTokenValid = jwtUtil.validateToken(refreshTokenRequest.getRefreshToken());

        if (isRefreshTokenValid) {
            boolean isAccessTokenExpired = jwtUtil.isTokenExpired(refreshTokenRequest.getAccessToken());
//This method checks if the provided refresh token is valid. It typically checks for its signature, expiration, and integrity.
            if (isAccessTokenExpired) {
                log.info("ACCESS TOKEN IS EXPIRED"); // Consider logging instead of printing to console.
                String newAccessToken = jwtUtil.doGenerateToken(jwtUtil.getSubject(refreshTokenRequest.getRefreshToken()));
                return new LoginResponse(newAccessToken, refreshTokenRequest.getRefreshToken());
            } else {
               log.info("ACCESS TOKEN IS NOT EXPIRED");
                return new LoginResponse(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken());
            }
        }

        return new LoginResponse();
    }
}
