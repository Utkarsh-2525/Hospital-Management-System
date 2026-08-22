package com.utkarsh2573.backend.laboratory.repository;

import com.utkarsh2573.backend.laboratory.entity.LabRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabRecommendationRepository
        extends JpaRepository<LabRecommendation, Long> {

    List<LabRecommendation> findByConsultationId(Long consultationId);
}