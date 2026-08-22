package com.utkarsh2573.backend.patient.dto;

import com.utkarsh2573.backend.prescription.entity.Prescription;

import java.time.LocalDateTime;
import java.util.List;

public record PatientPrescriptionResponse(

        Long prescriptionId,

        String prescriptionNumber,

        Long consultationId,

        Long visitId,

        String visitNumber,

        String doctorName,

        String status,

        String generalInstructions,

        LocalDateTime prescribedAt,

        List<Medicine> medicines

) {

    public record Medicine(

            Long medicineId,
            String medicineName,
            String dosage,
            String frequency,
            String duration,
            String instructions

    ) {}

    public static PatientPrescriptionResponse from(
            Prescription prescription
    ) {

        return new PatientPrescriptionResponse(

                prescription.getId(),

                prescription.getPrescriptionNumber(),

                prescription.getConsultation().getId(),

                prescription.getConsultation().getVisit().getId(),

                prescription.getConsultation().getVisit().getVisitNumber(),

                prescription.getDoctor().getFullName(),

                prescription.getStatus().name(),

                prescription.getGeneralInstructions(),

                prescription.getPrescribedAt(),

                prescription.getItems()
                        .stream()
                        .map(item -> new Medicine(
                                item.getMedicine().getId(),
                                item.getMedicine().getName(),
                                item.getDosage(),
                                item.getFrequency(),
                                item.getDuration(),
                                item.getInstructions()
                        ))
                        .toList()
        );
    }
}