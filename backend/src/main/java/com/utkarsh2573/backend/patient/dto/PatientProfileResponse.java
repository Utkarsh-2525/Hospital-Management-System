package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.patient.entity.Patient;

import java.time.LocalDate;

public record PatientProfileResponse(
        Long id,
        String patientNumber,
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String phone,
        String email,
        String address,
        String bloodGroup,
        String allergies,
        String emergencyContactName,
        String emergencyContactPhone,
        boolean active
) {

    public static PatientProfileResponse from(Patient patient) {
        return new PatientProfileResponse(
                patient.getId(),
                patient.getPatientNumber(),
                patient.getFullName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getBloodGroup(),
                patient.getAllergies(),
                patient.getEmergencyContactName(),
                patient.getEmergencyContactPhone(),
                patient.isActive()
        );
    }
}