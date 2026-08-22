package com.utkarsh2573.backend.prescription.dto;

import com.utkarsh2573.backend.common.enums.PrescriptionStatus;
import com.utkarsh2573.backend.prescription.entity.Prescription;

import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionResponse(
        Long id,
        String prescriptionNumber,
        Long consultationId,
        Long visitId,
        String visitNumber,
        Long patientId,
        String patientNumber,
        String patientName,
        Long doctorId,
        String doctorName,
        PrescriptionStatus status,
        String generalInstructions,
        LocalDateTime prescribedAt,
        List<Item> items
) {
    public record Item(
            Long medicineId,
            String medicineName,
            String dosage,
            String frequency,
            String duration,
            String instructions
    ) {}

    public static PrescriptionResponse from(Prescription p) {
        return new PrescriptionResponse(
                p.getId(),
                p.getPrescriptionNumber(),
                p.getConsultation().getId(),
                p.getConsultation().getVisit().getId(),
                p.getConsultation().getVisit().getVisitNumber(),
                p.getPatient().getId(),
                p.getPatient().getPatientNumber(),
                p.getPatient().getFullName(),
                p.getDoctor().getId(),
                p.getDoctor().getFullName(),
                p.getStatus(),
                p.getGeneralInstructions(),
                p.getPrescribedAt(),
                p.getItems().stream().map(i -> new Item(
                        i.getMedicine().getId(),
                        i.getMedicine().getName(),
                        i.getDosage(),
                        i.getFrequency(),
                        i.getDuration(),
                        i.getInstructions()
                )).toList()
        );
    }
}
