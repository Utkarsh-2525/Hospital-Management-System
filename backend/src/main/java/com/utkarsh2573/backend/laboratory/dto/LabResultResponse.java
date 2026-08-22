package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabResult;

import java.time.LocalDateTime;

public record LabResultResponse(
        Long id,

        Long labOrderId,
        String orderNumber,

        String testName,

        String result,
        String remarks,
        String attachmentUrl,

        LocalDateTime reportedAt
) {

    public static LabResultResponse from(LabResult labResult) {

        return new LabResultResponse(
                labResult.getId(),

                labResult.getLabOrder().getId(),
                labResult.getLabOrder().getOrderNumber(),

                labResult.getLabOrder()
                        .getLabTest()
                        .getName(),

                labResult.getResult(),
                labResult.getRemarks(),
                labResult.getAttachmentUrl(),

                labResult.getReportedAt()
        );
    }
}