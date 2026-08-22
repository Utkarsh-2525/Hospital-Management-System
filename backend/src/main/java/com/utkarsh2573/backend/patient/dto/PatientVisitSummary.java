package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.visit.entity.Visit;

public record PatientVisitSummary(
        Long visitId,
        String visitNumber,
        Long doctorId,
        String doctorName,
        String departmentName,
        String status
) {
    public static PatientVisitSummary from(Visit v) {
        return new PatientVisitSummary(
                v.getId(),
                v.getVisitNumber(),
                v.getDoctor().getId(),
                v.getDoctor().getFullName(),
                v.getDoctor().getDepartment().getName(),
                v.getStatus().name()
        );
    }
}
