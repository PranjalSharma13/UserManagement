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
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials provided.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(result.getName());
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getEmail()); String username = userDetails.getUsername(); // Modify based on your UserDetails implementation
        Set<Role> roles = userDetails.getAuthorities().stream()
                .map(authority -> {
                    RoleName roleName = RoleName.valueOf(authority.getAuthority());
                    return new Role(roleName);
                })
                .collect(Collectors.toSet());

        return new LoginResponse(username, roles,accessToken, refreshToken);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        boolean isRefreshTokenValid = jwtUtil.validateToken(refreshTokenRequest.getRefreshToken());

        if (isRefreshTokenValid) {
            boolean isAccessTokenExpired = jwtUtil.isTokenExpired(refreshTokenRequest.getAccessToken());

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
