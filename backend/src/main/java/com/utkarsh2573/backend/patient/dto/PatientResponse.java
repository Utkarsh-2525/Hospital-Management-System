package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.patient.entity.Patient;

import java.time.LocalDate;

public record PatientResponse(
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
        String emergencyContactPhone
) {
    public static PatientResponse from(Patient p) {
        return new PatientResponse(
                p.getId(), p.getPatientNumber(), p.getFullName(),
                p.getDateOfBirth(), p.getGender(), p.getPhone(), p.getEmail(),
                p.getAddress(), p.getBloodGroup(), p.getAllergies(),
                p.getEmergencyContactName(), p.getEmergencyContactPhone()
        );
    }
}
