package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabRecommendation;

public record LabRecommendationResponse(
        Long id,
        Long consultationId,
        Long labTestId,
        String testCode,
        String testName,
        String instructions
) {
    public static LabRecommendationResponse from(LabRecommendation r) {
        return new LabRecommendationResponse(
                r.getId(),
                r.getConsultation().getId(),
                r.getLabTest().getId(),
                r.getLabTest().getTestCode(),
                r.getLabTest().getName(),
                r.getInstructions()
        );
    }
}
