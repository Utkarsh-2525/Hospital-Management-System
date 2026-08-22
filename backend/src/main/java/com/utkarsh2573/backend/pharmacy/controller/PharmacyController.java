package com.utkarsh2573.backend.pharmacy.controller;

import com.utkarsh2573.backend.pharmacy.dto.*;
import com.utkarsh2573.backend.pharmacy.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping("/dispense")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public PharmacyDispenseResponse dispense(
            @Valid @RequestBody CreateDispenseRequest request) {
        return pharmacyService.dispense(request);
    }

    @GetMapping("/prescription/{prescriptionId}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST','DOCTOR','PATIENT')")
    public PharmacyDispenseResponse get(
            @PathVariable Long prescriptionId) {
        return pharmacyService.getByPrescription(prescriptionId);
    }
}
