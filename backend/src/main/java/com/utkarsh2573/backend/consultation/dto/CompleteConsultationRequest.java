package com.utkarsh2573.backend.consultation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompleteConsultationRequest(
        @NotBlank @Size(max = 2000) String chiefComplaint,
        @Size(max = 3000) String clinicalNotes,
        @NotBlank @Size(max = 2000) String diagnosis,
        @Size(max = 2000) String advice
) {}
