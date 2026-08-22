package com.utkarsh2573.backend.consultation.dto;

import com.utkarsh2573.backend.common.enums.ConsultationStatus;
import com.utkarsh2573.backend.consultation.entity.Consultation;

import java.time.LocalDateTime;

public record ConsultationResponse(
        Long id,
        Long visitId,
        String visitNumber,
        Long patientId,
        String patientNumber,
        String patientName,
        Long doctorId,
        String doctorName,
        ConsultationStatus status,
        String chiefComplaint,
        String clinicalNotes,
        String diagnosis,
        String advice,
        LocalDateTime startedAt,
        LocalDateTime completedAt
) {
    public static ConsultationResponse from(Consultation c) {
        return new ConsultationResponse(
                c.getId(),
                c.getVisit().getId(),
                c.getVisit().getVisitNumber(),
                c.getVisit().getPatient().getId(),
                c.getVisit().getPatient().getPatientNumber(),
                c.getVisit().getPatient().getFullName(),
                c.getDoctor().getId(),
                c.getDoctor().getFullName(),
                c.getStatus(),
                c.getChiefComplaint(),
                c.getClinicalNotes(),
                c.getDiagnosis(),
                c.getAdvice(),
                c.getStartedAt(),
                c.getCompletedAt()
        );
    }
}
