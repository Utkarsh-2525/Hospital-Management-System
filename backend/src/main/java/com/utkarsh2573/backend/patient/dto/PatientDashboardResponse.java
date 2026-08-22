package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.patient.entity.Patient;

import java.time.LocalDate;
import java.util.List;

public record PatientDashboardResponse(
        Long patientId,
        String patientNumber,
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String phone,
        String email,
        String bloodGroup,
        String allergies,
        List<PatientVisitSummary> visits
) {
    public static PatientDashboardResponse from(
            Patient patient,
            List<PatientVisitSummary> visits
    ) {
        return new PatientDashboardResponse(
                patient.getId(),
                patient.getPatientNumber(),
                patient.getFullName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getBloodGroup(),
                patient.getAllergies(),
                visits
        );
    }
}
