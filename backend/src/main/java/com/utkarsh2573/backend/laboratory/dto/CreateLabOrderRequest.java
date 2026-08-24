package com.utkarsh2573.backend.laboratory.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateLabOrderRequest(

        @NotNull
        Long patientId,

        @NotNull
        Long consultationId,

        @NotEmpty
        List<@NotNull Long> labTestIds,

        @Size(max = 500)
        String instructions
) {
}