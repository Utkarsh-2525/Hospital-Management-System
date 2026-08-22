package com.utkarsh2573.backend.pharmacy.repository;

import com.utkarsh2573.backend.pharmacy.entity.PharmacyDispense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PharmacyDispenseRepository
        extends JpaRepository<PharmacyDispense, Long> {

    Optional<PharmacyDispense> findByPrescriptionId(Long prescriptionId);

    List<PharmacyDispense> findByPatientIdOrderByIdDesc(Long patientId);
}