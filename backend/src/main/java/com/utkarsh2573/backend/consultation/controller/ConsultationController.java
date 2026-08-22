package com.utkarsh2573.backend.consultation.controller;

import com.utkarsh2573.backend.consultation.dto.*;
import com.utkarsh2573.backend.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/{visitId}/start")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse start(
            @PathVariable Long visitId,
            Authentication authentication) {
        return consultationService.start(visitId, authentication.getName());
    }

    @PutMapping("/{visitId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse complete(
            @PathVariable Long visitId,
            @Valid @RequestBody CompleteConsultationRequest request,
            Authentication authentication) {
        return consultationService.complete(
                visitId, request, authentication.getName());
    }

    @PutMapping("/{visitId}/vitals")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse vitals(
            @PathVariable Long visitId,
            @Valid @RequestBody VitalRequest request,
            Authentication authentication) {
        return consultationService.saveVitals(
                visitId, request, authentication.getName());
    }

    @GetMapping("/{visitId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse get(
            @PathVariable Long visitId,
            Authentication authentication) {
        return consultationService.get(visitId, authentication.getName());
    }
}
