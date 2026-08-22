package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.visit.entity.Visit;

public record PatientVisitSummary(
        Long visitId,
        String visitNumber,
        Long doctorId,
        String doctorName,
        Long departmentId,
        String departmentName,
        String visitDate,
        String status,
        String reason,
        String consultationFee
) {
    public static PatientVisitSummary from(Visit visit) {
        return new PatientVisitSummary(
                visit.getId(),
                visit.getVisitNumber(),
                visit.getDoctor().getId(),
                visit.getDoctor().getFullName(),
                visit.getDepartment().getId(),
                visit.getDepartment().getName(),
                visit.getVisitDate().toString(),
                visit.getStatus().name(),
                visit.getReason(),
                visit.getConsultationFee().toString()
        );
    }
}
