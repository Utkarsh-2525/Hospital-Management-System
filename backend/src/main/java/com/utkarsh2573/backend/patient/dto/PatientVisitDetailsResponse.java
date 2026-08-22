package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.visit.entity.Visit;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PatientVisitDetailsResponse(
        Long visitId,
        String visitNumber,

        Long patientId,
        String patientNumber,
        String patientName,

        Long doctorId,
        String doctorName,

        Long departmentId,
        String departmentName,

        LocalDate visitDate,
        String status,
        BigDecimal consultationFee,
        String reason
) {

    public static PatientVisitDetailsResponse from(Visit visit) {
        return new PatientVisitDetailsResponse(
                visit.getId(),
                visit.getVisitNumber(),

                visit.getPatient().getId(),
                visit.getPatient().getPatientNumber(),
                visit.getPatient().getFullName(),

                visit.getDoctor().getId(),
                visit.getDoctor().getFullName(),

                visit.getDepartment().getId(),
                visit.getDepartment().getName(),

                visit.getVisitDate(),
                visit.getStatus().name(),
                visit.getConsultationFee(),
                visit.getReason()
        );
    }
}