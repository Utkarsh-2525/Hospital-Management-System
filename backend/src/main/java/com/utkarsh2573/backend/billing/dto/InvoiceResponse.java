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
        BigDecimal consultationFee,
        BigDecimal registrationFee,
        BigDecimal amount,
        BigDecimal paidAmount,
        BigDecimal outstandingAmount,
        InvoiceStatus status
) {

    public static InvoiceResponse from(Invoice invoice) {

        BigDecimal outstandingAmount = invoice.getAmount()
                .subtract(invoice.getPaidAmount());

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getVisit().getId(),
                invoice.getVisit().getVisitNumber(),
                invoice.getPatient().getPatientNumber(),
                invoice.getPatient().getFullName(),
                invoice.getConsultationFee(),
                invoice.getRegistrationFee(),
                invoice.getAmount(),
                invoice.getPaidAmount(),
                outstandingAmount,
                invoice.getStatus()
        );
    }
}
