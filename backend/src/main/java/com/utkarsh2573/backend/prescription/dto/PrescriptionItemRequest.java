package com.utkarsh2573.backend.prescription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrescriptionItemRequest(
        @NotNull Long medicineId,
        @NotBlank @Size(max = 100) String dosage,
        @NotBlank @Size(max = 100) String frequency,
        @NotBlank @Size(max = 100) String duration,
        @Size(max = 500) String instructions
) {}
