package com.utkarsh2573.backend.visit.dto;

import jakarta.validation.constraints.*;

public record CreateVisitRequest(
        @NotNull Long patientId,
        @NotNull Long doctorId,
        @Size(max = 500) String reason
) {}
