package com.utkarsh2573.backend.auth.dto;

public record LoginResponse(
        String token,
        Long userId,
        String username,
        String role
) {}
