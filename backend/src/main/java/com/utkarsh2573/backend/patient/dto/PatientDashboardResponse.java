package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.patient.entity.Patient;

import java.util.List;

public record PatientDashboardResponse(
        Long patientId,
        String patientNumber,
        String patientName,
        List<PatientVisitSummary> visits
) {
    public static PatientDashboardResponse from(
            Patient patient,
            List<PatientVisitSummary> visits) {
        return new PatientDashboardResponse(
                patient.getId(),
                patient.getPatientNumber(),
                patient.getFullName(),
                visits
        );
    }
}
