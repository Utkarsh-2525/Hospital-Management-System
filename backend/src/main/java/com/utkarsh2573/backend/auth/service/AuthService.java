package com.utkarsh2573.backend.auth.service;

import com.utkarsh2573.backend.auth.dto.*;
import com.utkarsh2573.backend.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails details =
                userDetailsService.loadUserByUsername(request.username());

        CustomUserDetails user = (CustomUserDetails) details;

        String role = user.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return new LoginResponse(
                jwtService.generateToken(user),
                user.getId(),
                user.getUsername(),
                role
        );
    }
}
