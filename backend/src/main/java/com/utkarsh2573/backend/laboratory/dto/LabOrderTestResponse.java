package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabOrderItem;

public record LabOrderTestResponse(
        Long labTestId,
        String testCode,
        String testName,
        String sampleType
) {

    public static LabOrderTestResponse from(LabOrderItem item) {

        return new LabOrderTestResponse(
                item.getLabTest().getId(),
                item.getLabTest().getTestCode(),
                item.getLabTest().getName(),
                item.getLabTest().getSampleType()
        );
    }
}