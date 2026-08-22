package com.utkarsh2573.backend.patient.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreatePatientRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Size(max = 20) String gender,
        @NotBlank @Size(max = 20) String phone,
        @Email @Size(max = 120) String email,
        @Size(max = 500) String address,
        @Size(max = 20) String bloodGroup,
        @Size(max = 500) String allergies,
        @Size(max = 120) String emergencyContactName,
        @Size(max = 20) String emergencyContactPhone
) {}
