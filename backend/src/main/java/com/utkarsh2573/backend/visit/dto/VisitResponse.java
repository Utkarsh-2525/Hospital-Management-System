package com.utkarsh2573.backend.visit.dto;

import com.utkarsh2573.backend.common.enums.VisitStatus;
import com.utkarsh2573.backend.visit.entity.Visit;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VisitResponse(
        Long id,
        String visitNumber,
        Long patientId,
        String patientNumber,
        String patientName,
        Long doctorId,
        String doctorName,
        Long departmentId,
        String departmentName,
        LocalDate visitDate,
        VisitStatus status,
        BigDecimal consultationFee,
        String reason
) {
    public static VisitResponse from(Visit v) {
        return new VisitResponse(
                v.getId(), v.getVisitNumber(),
                v.getPatient().getId(), v.getPatient().getPatientNumber(),
                v.getPatient().getFullName(),
                v.getDoctor().getId(), v.getDoctor().getFullName(),
                v.getDepartment().getId(), v.getDepartment().getName(),
                v.getVisitDate(), v.getStatus(), v.getConsultationFee(),
                v.getReason()
        );
    }
}
