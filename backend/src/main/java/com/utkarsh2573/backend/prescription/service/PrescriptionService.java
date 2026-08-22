package com.utkarsh2573.backend.prescription.service;

import com.utkarsh2573.backend.common.enums.ConsultationStatus;
import com.utkarsh2573.backend.consultation.entity.Consultation;
import com.utkarsh2573.backend.consultation.repository.ConsultationRepository;
import com.utkarsh2573.backend.exception.*;
import com.utkarsh2573.backend.medicine.entity.Medicine;
import com.utkarsh2573.backend.medicine.repository.MedicineRepository;
import com.utkarsh2573.backend.prescription.dto.*;
import com.utkarsh2573.backend.prescription.entity.*;
import com.utkarsh2573.backend.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final MedicineRepository medicineRepository;

    @Transactional
    public PrescriptionResponse create(
            CreatePrescriptionRequest request,
            String username
    ) {
        Consultation consultation = consultationRepository.findById(request.consultationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found: " + request.consultationId()));

        if (!consultation.getDoctor().getUser().getUsername().equals(username)) {
            throw new BadRequestException("You are not assigned to this consultation");
        }

        if (consultation.getStatus() != ConsultationStatus.COMPLETED) {
            throw new BadRequestException(
                    "Prescription can be created only after consultation is completed");
        }

        if (prescriptionRepository.findByConsultationId(consultation.getId()).isPresent()) {
            throw new BadRequestException("Prescription already exists for this consultation");
        }

        Prescription prescription = Prescription.builder()
                .prescriptionNumber("RX-" + UUID.randomUUID().toString()
                        .replace("-", "").substring(0, 12).toUpperCase())
                .consultation(consultation)
                .doctor(consultation.getDoctor())
                .patient(consultation.getVisit().getPatient())
                .generalInstructions(request.generalInstructions())
                .build();

        if (request.items() != null) {
            for (PrescriptionItemRequest itemRequest : request.items()) {
                Medicine medicine = medicineRepository.findById(itemRequest.medicineId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Medicine not found: " + itemRequest.medicineId()));

                if (!medicine.isActive()) {
                    throw new BadRequestException(
                            "Medicine is inactive: " + medicine.getName());
                }

                prescription.getItems().add(
                        PrescriptionItem.builder()
                                .prescription(prescription)
                                .medicine(medicine)
                                .dosage(itemRequest.dosage())
                                .frequency(itemRequest.frequency())
                                .duration(itemRequest.duration())
                                .instructions(itemRequest.instructions())
                                .build()
                );
            }
        }

        return PrescriptionResponse.from(prescriptionRepository.save(prescription));
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getByConsultation(
            Long consultationId,
            String username
    ) {
        Prescription p = prescriptionRepository.findByConsultationId(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found for consultation: " + consultationId));

        if (!p.getDoctor().getUser().getUsername().equals(username)
                && !p.getPatient().getUser().getUsername().equals(username)) {
            throw new BadRequestException("You are not allowed to view this prescription");
        }

        return PrescriptionResponse.from(p);
    }
}
