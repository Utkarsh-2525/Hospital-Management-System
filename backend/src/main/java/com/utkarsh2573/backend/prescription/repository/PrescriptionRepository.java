package com.utkarsh2573.backend.prescription.repository;

import com.utkarsh2573.backend.prescription.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findByConsultationId(Long consultationId);

    Optional<Prescription> findByPrescriptionNumber(String prescriptionNumber);

    List<Prescription> findByPatientIdOrderByPrescribedAtDesc(Long patientId);
}