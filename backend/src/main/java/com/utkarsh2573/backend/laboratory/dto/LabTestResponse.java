package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabTest;

public record LabTestResponse(
        Long id,
        String testCode,
        String name,
        String description,
        String sampleType
) {
    public static LabTestResponse from(LabTest t) {
        return new LabTestResponse(
                t.getId(), t.getTestCode(), t.getName(),
                t.getDescription(), t.getSampleType()
        );
    }
}
