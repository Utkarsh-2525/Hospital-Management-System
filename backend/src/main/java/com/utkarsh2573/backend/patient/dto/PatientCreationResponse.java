package com.utkarsh2573.backend.patient.dto;

public record PatientCreationResponse(
        PatientResponse patient,
        String username,
        String temporaryPassword
) {
}