package com.utkarsh2573.backend.doctor.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateDoctorRequest(

        @NotBlank
        @Size(max = 120)
        String fullName,

        @NotBlank
        @Size(max = 100)
        String username,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotBlank
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 100)
        String qualification,

        @NotBlank
        @Size(max = 100)
        String specialization,

        @NotNull
        Long departmentId,

        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal consultationFee
) {
}