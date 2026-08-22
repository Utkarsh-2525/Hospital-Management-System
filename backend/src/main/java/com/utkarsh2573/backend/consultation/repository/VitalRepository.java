package com.utkarsh2573.backend.consultation.repository;

import com.utkarsh2573.backend.consultation.entity.Vital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VitalRepository extends JpaRepository<Vital, Long> {
    Optional<Vital> findByConsultationId(Long consultationId);
}
