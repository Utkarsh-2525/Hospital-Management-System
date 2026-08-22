package com.utkarsh2573.backend.prescription.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePrescriptionRequest(
        @NotNull Long consultationId,
        @Size(max = 2000) String generalInstructions,
        @Valid List<PrescriptionItemRequest> items
) {}
