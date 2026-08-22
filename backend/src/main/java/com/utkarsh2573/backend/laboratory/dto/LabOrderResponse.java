package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabOrder;

import java.time.LocalDateTime;

public record LabOrderResponse(
        Long id,
        String orderNumber,

        Long patientId,
        String patientNumber,
        String patientName,

        Long consultationId,
        Long labTestId,
        String testCode,
        String testName,
        String sampleType,

        String status,
        String instructions,

        LocalDateTime orderedAt,
        LocalDateTime sampleCollectedAt,
        LocalDateTime completedAt
) {

    public static LabOrderResponse from(LabOrder order) {

        return new LabOrderResponse(
                order.getId(),
                order.getOrderNumber(),

                order.getPatient().getId(),
                order.getPatient().getPatientNumber(),
                order.getPatient().getFullName(),

                order.getConsultation().getId(),

                order.getLabTest().getId(),
                order.getLabTest().getTestCode(),
                order.getLabTest().getName(),
                order.getLabTest().getSampleType(),

                order.getStatus().name(),
                order.getInstructions(),

                order.getOrderedAt(),
                order.getSampleCollectedAt(),
                order.getCompletedAt()
        );
    }
}