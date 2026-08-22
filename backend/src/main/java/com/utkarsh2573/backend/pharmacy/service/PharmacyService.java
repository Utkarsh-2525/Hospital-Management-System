package com.utkarsh2573.backend.pharmacy.service;

import com.utkarsh2573.backend.common.enums.DispenseStatus;
import com.utkarsh2573.backend.exception.*;
import com.utkarsh2573.backend.medicine.entity.Medicine;
import com.utkarsh2573.backend.medicine.repository.MedicineRepository;
import com.utkarsh2573.backend.pharmacy.dto.*;
import com.utkarsh2573.backend.pharmacy.entity.*;
import com.utkarsh2573.backend.pharmacy.repository.PharmacyDispenseRepository;
import com.utkarsh2573.backend.prescription.entity.Prescription;
import com.utkarsh2573.backend.prescription.entity.PrescriptionItem;
import com.utkarsh2573.backend.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final PrescriptionRepository prescriptionRepository;
    private final PharmacyDispenseRepository dispenseRepository;
    private final MedicineRepository medicineRepository;

    @Transactional
    public PharmacyDispenseResponse dispense(CreateDispenseRequest request) {
        Prescription prescription = prescriptionRepository.findById(request.prescriptionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found: " + request.prescriptionId()));

        if (dispenseRepository.findByPrescriptionId(prescription.getId()).isPresent()) {
            throw new BadRequestException("This prescription has already been received by pharmacy");
        }

        PharmacyDispense dispense = PharmacyDispense.builder()
                .dispenseNumber("RXD-" + UUID.randomUUID().toString()
                        .replace("-", "").substring(0, 12).toUpperCase())
                .prescription(prescription)
                .patient(prescription.getPatient())
                .pharmacistNotes(request.pharmacistNotes())
                .status(DispenseStatus.PENDING)
                .build();

        int total = prescription.getItems().size();
        int completed = 0;

        for (PrescriptionItem prescriptionItem : prescription.getItems()) {
            int prescribedQuantity = estimateQuantity(prescriptionItem.getDuration());
            int dispensedQuantity = 0;

            if (request.items() != null) {
                for (CreateDispenseRequest.Item item : request.items()) {
                    if (item.medicineId().equals(prescriptionItem.getMedicine().getId())) {
                        dispensedQuantity = Math.max(0, item.dispensedQuantity());
                        if (dispensedQuantity > prescribedQuantity) {
                            throw new BadRequestException(
                                    "Dispensed quantity cannot exceed prescribed quantity for "
                                            + prescriptionItem.getMedicine().getName());
                        }
                        dispense.getItems().add(
                                PharmacyDispenseItem.builder()
                                        .dispense(dispense)
                                        .medicine(prescriptionItem.getMedicine())
                                        .prescribedQuantity(prescribedQuantity)
                                        .dispensedQuantity(dispensedQuantity)
                                        .notes(item.notes())
                                        .build()
                        );
                        break;
                    }
                }
            }

            if (dispensedQuantity == prescribedQuantity) {
                completed++;
            }
        }

        if (completed == total && total > 0) {
            dispense.setStatus(DispenseStatus.DISPENSED);
            dispense.setDispensedAt(LocalDateTime.now());
        } else if (completed > 0) {
            dispense.setStatus(DispenseStatus.PARTIALLY_DISPENSED);
        } else {
            dispense.setStatus(DispenseStatus.PENDING);
        }

        return PharmacyDispenseResponse.from(dispenseRepository.save(dispense));
    }

    @Transactional(readOnly = true)
    public PharmacyDispenseResponse getByPrescription(Long prescriptionId) {
        return PharmacyDispenseResponse.from(
                dispenseRepository.findByPrescriptionId(prescriptionId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Pharmacy record not found for prescription: " + prescriptionId))
        );
    }

    private int estimateQuantity(String duration) {
        if (duration == null || duration.isBlank()) {
            return 1;
        }
        String digits = duration.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return 1;
        }
        int days = Math.max(1, Integer.parseInt(digits));
        return Math.min(days, 999);
    }
}
