package com.utkarsh2573.backend.patient.service;

import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.patient.dto.*;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public PatientResponse create(CreatePatientRequest request) {
        if (patientRepository.existsByPhone(request.phone())) {
            throw new BadRequestException(
                    "Patient with phone number " + request.phone() + " already exists"
            );
        }

        if (request.email() != null
                && !request.email().isBlank()
                && patientRepository.existsByEmail(request.email())) {

            throw new BadRequestException(
                    "Patient with email " + request.email() + " already exists"
            );
        }

        Patient patient = Patient.builder()
                .patientNumber(generatePatientNumber())
                .fullName(request.fullName())
                .dateOfBirth(request.dateOfBirth())
                .gender(request.gender())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .bloodGroup(request.bloodGroup())
                .allergies(request.allergies())
                .emergencyContactName(request.emergencyContactName())
                .emergencyContactPhone(request.emergencyContactPhone())
                .build();

        return PatientResponse.from(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public PatientResponse get(Long id) {
        return PatientResponse.from(patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id)));
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> search(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());

        Page<Patient> patients = query == null || query.isBlank()
                ? patientRepository.findAll(pageable)
                : patientRepository.findByFullNameContainingIgnoreCaseOrPhoneContaining(
                        query, query, pageable);

        return patients.map(PatientResponse::from);
    }

    private String generatePatientNumber() {
        String number;
        do {
            number = "P-" + UUID.randomUUID().toString()
                    .replace("-", "").substring(0, 8).toUpperCase();
        } while (patientRepository.existsByPatientNumber(number));
        return number;
    }


}
