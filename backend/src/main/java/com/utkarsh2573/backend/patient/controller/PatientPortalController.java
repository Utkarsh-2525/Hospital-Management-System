package com.utkarsh2573.backend.patient.controller;

import com.utkarsh2573.backend.laboratory.dto.LabOrderResponse;
import com.utkarsh2573.backend.laboratory.dto.LabResultResponse;
import com.utkarsh2573.backend.patient.dto.PatientDashboardResponse;
import com.utkarsh2573.backend.patient.dto.PatientMedicalRecordResponse;
import com.utkarsh2573.backend.patient.dto.PatientPrescriptionResponse;
import com.utkarsh2573.backend.patient.dto.PatientVisitSummary;
import com.utkarsh2573.backend.patient.service.PatientPortalService;
import com.utkarsh2573.backend.pharmacy.dto.PharmacyDispenseResponse;
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

    @GetMapping("/prescriptions")
    @PreAuthorize("hasRole('PATIENT')")
    public List<PatientPrescriptionResponse> prescriptions(
            Authentication authentication
    ) {
        return patientPortalService.getPrescriptions(
                authentication.getName()
        );
    }

    @GetMapping("/pharmacy")
    @PreAuthorize("hasRole('PATIENT')")
    public List<PharmacyDispenseResponse> pharmacy(
            Authentication authentication
    ) {
        return patientPortalService.getPharmacyHistory(
                authentication.getName()
        );
    }

    @GetMapping("/lab-orders")
    @PreAuthorize("hasRole('PATIENT')")
    public List<LabOrderResponse> labOrders(
            Authentication authentication
    ) {
        return patientPortalService.getLabOrders(
                authentication.getName()
        );
    }

    @GetMapping("/lab-orders/{orderId}/result")
    @PreAuthorize("hasRole('PATIENT')")
    public LabResultResponse labResult(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return patientPortalService.getLabResult(
                authentication.getName(),
                orderId
        );
    }

    @GetMapping("/medical-record")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientMedicalRecordResponse medicalRecord(
            Authentication authentication
    ) {
        return patientPortalService.getMedicalRecord(
                authentication.getName()
        );
    }
}
