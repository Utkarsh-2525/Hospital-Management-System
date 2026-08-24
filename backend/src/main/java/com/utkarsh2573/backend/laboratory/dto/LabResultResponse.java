package com.utkarsh2573.backend.laboratory.dto;

import com.utkarsh2573.backend.laboratory.entity.LabResult;

import java.time.LocalDateTime;

public record LabResultResponse(
        Long id,

        Long labOrderId,
        String orderNumber,

        Long labTestId,
        String testCode,
        String testName,

        String result,
        String remarks,
        String attachmentUrl,

        LocalDateTime reportedAt
) {

    public static LabResultResponse from(LabResult labResult) {

        var item = labResult.getLabOrderItem();

        return new LabResultResponse(
                labResult.getId(),

                item.getLabOrder().getId(),
                item.getLabOrder().getOrderNumber(),

                item.getLabTest().getId(),
                item.getLabTest().getTestCode(),
                item.getLabTest().getName(),

                labResult.getResult(),
                labResult.getRemarks(),
                labResult.getAttachmentUrl(),

                labResult.getReportedAt()
        );
    }
}