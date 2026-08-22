package com.utkarsh2573.backend.patient.controller;

import com.utkarsh2573.backend.patient.dto.PatientDashboardResponse;
import com.utkarsh2573.backend.patient.dto.PatientVisitSummary;
import com.utkarsh2573.backend.patient.service.PatientPortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient-portal")
@RequiredArgsConstructor
public class PatientPortalController {

    private final PatientPortalService patientPortalService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientDashboardResponse dashboard(Authentication authentication) {
        return patientPortalService.getDashboard(authentication.getName());
    }

    @GetMapping("/visits")
    @PreAuthorize("hasRole('PATIENT')")
    public List<PatientVisitSummary> visits(Authentication authentication) {
        return patientPortalService.getVisits(authentication.getName());
    }
}
