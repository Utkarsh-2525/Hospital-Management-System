package com.utkarsh2573.backend.prescription.controller;

import com.utkarsh2573.backend.prescription.dto.*;
import com.utkarsh2573.backend.prescription.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public PrescriptionResponse create(
            @Valid @RequestBody CreatePrescriptionRequest request,
            Authentication authentication) {
        return prescriptionService.create(
                request, authentication.getName());
    }

    @GetMapping("/consultation/{consultationId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','PHARMACIST')")
    public PrescriptionResponse get(
            @PathVariable Long consultationId,
            Authentication authentication) {
        return prescriptionService.getByConsultation(
                consultationId, authentication.getName());
    }
}
