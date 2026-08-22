package com.utkarsh2573.backend.laboratory.service;

import com.utkarsh2573.backend.consultation.entity.Consultation;
import com.utkarsh2573.backend.consultation.repository.ConsultationRepository;
import com.utkarsh2573.backend.exception.*;
import com.utkarsh2573.backend.laboratory.dto.*;
import com.utkarsh2573.backend.laboratory.entity.*;
import com.utkarsh2573.backend.laboratory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabRecommendationService {

    private final ConsultationRepository consultationRepository;
    private final LabTestRepository labTestRepository;
    private final LabRecommendationRepository recommendationRepository;

    @Transactional
    public LabRecommendationResponse recommend(
            LabRecommendationRequest request,
            String username
    ) {
        Consultation consultation = consultationRepository.findById(request.consultationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found: " + request.consultationId()));

        if (!consultation.getDoctor().getUser().getUsername().equals(username)) {
            throw new BadRequestException("You are not assigned to this consultation");
        }

        LabTest test = labTestRepository.findById(request.labTestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lab test not found: " + request.labTestId()));

        LabRecommendation recommendation = LabRecommendation.builder()
                .consultation(consultation)
                .labTest(test)
                .instructions(request.instructions())
                .build();

        return LabRecommendationResponse.from(
                recommendationRepository.save(recommendation));
    }

    @Transactional(readOnly = true)
    public java.util.List<LabRecommendationResponse> list(Long consultationId) {
        return recommendationRepository.findByConsultationId(consultationId)
                .stream()
                .map(LabRecommendationResponse::from)
                .toList();
    }
}
