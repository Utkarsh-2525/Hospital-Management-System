package com.utkarsh2573.backend.visit.dto;

import com.utkarsh2573.backend.common.enums.QueueStatus;
import com.utkarsh2573.backend.visit.entity.VisitQueue;

import java.time.LocalDate;

public record QueueResponse(
        Long queueId,
        Long visitId,
        String visitNumber,
        String patientNumber,
        String patientName,
        Long doctorId,
        String doctorName,
        LocalDate queueDate,
        Integer queueNumber,
        QueueStatus status
) {
    public static QueueResponse from(VisitQueue q) {
        return new QueueResponse(
                q.getId(), q.getVisit().getId(), q.getVisit().getVisitNumber(),
                q.getVisit().getPatient().getPatientNumber(),
                q.getVisit().getPatient().getFullName(),
                q.getDoctor().getId(), q.getDoctor().getFullName(),
                q.getQueueDate(), q.getQueueNumber(), q.getStatus()
        );
    }
}
