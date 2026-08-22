package com.utkarsh2573.backend.pharmacy.dto;

import com.utkarsh2573.backend.common.enums.DispenseStatus;
import com.utkarsh2573.backend.pharmacy.entity.PharmacyDispense;

import java.time.LocalDateTime;
import java.util.List;

public record PharmacyDispenseResponse(
        Long id,
        String dispenseNumber,
        Long prescriptionId,
        String prescriptionNumber,
        Long patientId,
        String patientNumber,
        String patientName,
        DispenseStatus status,
        String pharmacistNotes,
        LocalDateTime dispensedAt,
        List<Item> items
) {
    public record Item(
            Long medicineId,
            String medicineName,
            Integer prescribedQuantity,
            Integer dispensedQuantity,
            String notes
    ) {}

    public static PharmacyDispenseResponse from(PharmacyDispense d) {
        return new PharmacyDispenseResponse(
                d.getId(),
                d.getDispenseNumber(),
                d.getPrescription().getId(),
                d.getPrescription().getPrescriptionNumber(),
                d.getPatient().getId(),
                d.getPatient().getPatientNumber(),
                d.getPatient().getFullName(),
                d.getStatus(),
                d.getPharmacistNotes(),
                d.getDispensedAt(),
                d.getItems().stream().map(i -> new Item(
                        i.getMedicine().getId(),
                        i.getMedicine().getName(),
                        i.getPrescribedQuantity(),
                        i.getDispensedQuantity(),
                        i.getNotes()
                )).toList()
        );
    }
}
