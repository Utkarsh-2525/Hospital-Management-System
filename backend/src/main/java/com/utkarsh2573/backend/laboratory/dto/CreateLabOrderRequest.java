package com.utkarsh2573.backend.laboratory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLabOrderRequest(

        @NotNull
        Long patientId,

        @NotNull
        Long consultationId,

        @NotNull
        Long labTestId,

        @Size(max = 500)
        String instructions
) {
}