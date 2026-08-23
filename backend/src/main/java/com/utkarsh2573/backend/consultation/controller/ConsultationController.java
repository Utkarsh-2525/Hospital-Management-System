package com.utkarsh2573.backend.consultation.controller;

import com.utkarsh2573.backend.consultation.dto.*;
import com.utkarsh2573.backend.consultation.service.ConsultationService;
import com.utkarsh2573.backend.visit.dto.VisitResponse;
import com.utkarsh2573.backend.visit.dto.QueueResponse;
import com.utkarsh2573.backend.visit.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/doctor/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;
    private final VisitService visitService;

    @GetMapping("/visits")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<VisitResponse> getDoctorVisits(
            Authentication authentication
    ) {
        return visitService.getDoctorVisits(
                authentication.getName()
        );
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<QueueResponse> queue(
            Authentication authentication
    ) {
        return visitService.myDoctorQueue(
                authentication.getName(),
                java.time.LocalDate.now()
        );
    }

    @PostMapping("/{visitId}/start")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse start(
            @PathVariable Long visitId,
            Authentication authentication
    ) {
        return consultationService.start(
                visitId,
                authentication.getName()
        );
    }

    @PutMapping("/{visitId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse complete(
            @PathVariable Long visitId,
            @Valid @RequestBody CompleteConsultationRequest request,
            Authentication authentication
    ) {
        return consultationService.complete(
                visitId,
                request,
                authentication.getName()
        );
    }

    @PutMapping("/{visitId}/vitals")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse vitals(
            @PathVariable Long visitId,
            @Valid @RequestBody VitalRequest request,
            Authentication authentication
    ) {
        return consultationService.saveVitals(
                visitId,
                request,
                authentication.getName()
        );
    }

    @GetMapping("/{visitId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ConsultationResponse get(
            @PathVariable Long visitId,
            Authentication authentication
    ) {
        return consultationService.get(
                visitId,
                authentication.getName()
        );
    }

//    @GetMapping("/debug-auth")
//    public Map<String, Object> debugAuth(Authentication authentication) {
//
//        return Map.of(
//                "username", authentication.getName(),
//                "authorities", authentication.getAuthorities()
//                        .stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .toList(),
//                "authenticated", authentication.isAuthenticated()
//        );
//    }
}