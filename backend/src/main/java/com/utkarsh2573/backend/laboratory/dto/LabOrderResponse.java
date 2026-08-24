package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabOrder;

import java.time.LocalDateTime;
import java.util.List;

public record LabOrderResponse(
        Long id,
        String orderNumber,

        Long patientId,
        String patientNumber,
        String patientName,

        Long consultationId,

        List<LabOrderTestResponse> tests,

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

                order.getItems()
                        .stream()
                        .map(LabOrderTestResponse::from)
                        .toList(),

                order.getStatus().name(),
                order.getInstructions(),

                order.getOrderedAt(),
                order.getSampleCollectedAt(),
                order.getCompletedAt()
        );
    }
}