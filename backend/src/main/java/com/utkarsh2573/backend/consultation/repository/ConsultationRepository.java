package com.utkarsh2573.backend.consultation.repository;

import com.utkarsh2573.backend.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findByVisitId(Long visitId);
}
