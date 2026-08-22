package com.utkarsh2573.backend.patient.repository;

import com.utkarsh2573.backend.patient.entity.Patient;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientNumber(String patientNumber);

    boolean existsByPatientNumber(String patientNumber);

    Page<Patient> findByFullNameContainingIgnoreCaseOrPhoneContaining(
            String name,
            String phone,
            Pageable pageable
    );
}
