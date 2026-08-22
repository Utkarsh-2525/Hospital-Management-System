package com.utkarsh2573.backend.billing.service;

import com.utkarsh2573.backend.billing.dto.*;
import com.utkarsh2573.backend.billing.entity.*;
import com.utkarsh2573.backend.billing.repository.*;
import com.utkarsh2573.backend.common.enums.*;
import com.utkarsh2573.backend.exception.*;
import com.utkarsh2573.backend.visit.entity.Visit;
import com.utkarsh2573.backend.visit.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final VisitRepository visitRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public InvoiceResponse createConsultationInvoice(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visit not found: " + visitId));

        if (invoiceRepository.findByVisitId(visitId).isPresent()) {
            throw new BadRequestException("Invoice already exists for this visit");
        }

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .patient(visit.getPatient())
                .visit(visit)
                .amount(visit.getConsultationFee())
                .paidAmount(BigDecimal.ZERO)
                .status(InvoiceStatus.UNPAID)
                .build();

        return InvoiceResponse.from(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceResponse pay(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.invoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found: " + request.invoiceId()));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice is already fully paid");
        }

        BigDecimal remaining = invoice.getAmount().subtract(invoice.getPaidAmount());

        if (request.amount().compareTo(remaining) > 0) {
            throw new BadRequestException(
                    "Payment exceeds remaining amount: " + remaining);
        }

        Payment payment = Payment.builder()
                .paymentNumber(generatePaymentNumber())
                .invoice(invoice)
                .amount(request.amount())
                .method(request.method())
                .status(PaymentStatus.SUCCESS)
                .transactionReference(request.transactionReference())
                .build();

        paymentRepository.save(payment);

        invoice.setPaidAmount(invoice.getPaidAmount().add(request.amount()));

        if (invoice.getPaidAmount().compareTo(invoice.getAmount()) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.getVisit().setStatus(VisitStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        return InvoiceResponse.from(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getByVisit(Long visitId) {
        return InvoiceResponse.from(invoiceRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found for visit: " + visitId)));
    }

    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
    }

    private String generatePaymentNumber() {
        return "PAY-" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
    }
}
