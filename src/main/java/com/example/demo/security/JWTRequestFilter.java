package com.example.demo.security;

import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTRequestFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
// This utility class is responsible for handling JWT (JSON Web Tokens), including validation and extraction of user information
    private final UserDetailsService userDetailsService;
// This service loads user-specific data (like roles and credentials).
    public JWTRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);//to get the JWT from the Authorization header.

        if (token != null && jwtUtil.validateToken(token)) {
            SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(token));//sets the authentication in the SecurityContext. This means the user is authenticated for this request.
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Helper method
     *
     * @param request
     * @return
     */

    // This method checks the Authorization header for a Bearer token.
    //If found, it extracts the token (removing the "Bearer " prefix) and returns it. If not found, it returns null
    public String extractTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return token;
        }
        return null;
    }
}
