package com.utkarsh2573.backend.billing.controller;

import com.utkarsh2573.backend.billing.dto.*;
import com.utkarsh2573.backend.billing.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/visits/{visitId}/invoice")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<InvoiceResponse> createInvoice(
            @PathVariable Long visitId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(billingService.createConsultationInvoice(visitId));
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public InvoiceResponse pay(@Valid @RequestBody PaymentRequest request) {
        return billingService.pay(request);
    }

    @GetMapping("/visits/{visitId}/invoice")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR')")
    public InvoiceResponse getByVisit(@PathVariable Long visitId) {
        return billingService.getByVisit(visitId);
    }
}
