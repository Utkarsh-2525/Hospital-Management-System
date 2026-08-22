package com.utkarsh2573.backend.billing.dto;

import com.utkarsh2573.backend.billing.entity.Invoice;
import com.utkarsh2573.backend.common.enums.InvoiceStatus;

import java.math.BigDecimal;

public record InvoiceResponse(
        Long id,
        String invoiceNumber,
        Long visitId,
        String visitNumber,
        String patientNumber,
        String patientName,
        BigDecimal amount,
        BigDecimal paidAmount,
        InvoiceStatus status
) {
    public static InvoiceResponse from(Invoice i) {
        return new InvoiceResponse(
                i.getId(), i.getInvoiceNumber(),
                i.getVisit().getId(), i.getVisit().getVisitNumber(),
                i.getPatient().getPatientNumber(),
                i.getPatient().getFullName(),
                i.getAmount(), i.getPaidAmount(), i.getStatus()
        );
    }
}
