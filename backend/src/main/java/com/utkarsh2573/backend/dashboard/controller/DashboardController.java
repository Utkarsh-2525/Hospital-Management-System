package com.utkarsh2573.backend.dashboard.controller;

import com.utkarsh2573.backend.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public Map<String, Object> adminDashboard(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return Map.of(
                "message", "Admin dashboard access granted",
                "username", user.getUsername(),
                "role", "ADMIN"
        );
    }

    @GetMapping("/receptionist/dashboard")
    public Map<String, Object> receptionistDashboard(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return Map.of(
                "message", "Receptionist dashboard access granted",
                "username", user.getUsername(),
                "role", "RECEPTIONIST"
        );
    }

    @GetMapping("/doctor/dashboard")
    public Map<String, Object> doctorDashboard(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return Map.of(
                "message", "Doctor dashboard access granted",
                "username", user.getUsername(),
                "role", "DOCTOR"
        );
    }

    @GetMapping("/pharmacy/dashboard")
    public Map<String, Object> pharmacyDashboard(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return Map.of(
                "message", "Pharmacy dashboard access granted",
                "username", user.getUsername(),
                "role", "PHARMACIST"
        );
    }

    @GetMapping("/lab/dashboard")
    public Map<String, Object> labDashboard(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return Map.of(
                "message", "Laboratory dashboard access granted",
                "username", user.getUsername(),
                "role", "LAB_TECHNICIAN"
        );
    }

    @GetMapping("/patient/dashboard")
    public Map<String, Object> patientDashboard(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return Map.of(
                "message", "Patient dashboard access granted",
                "username", user.getUsername(),
                "role", "PATIENT"
        );
    }
}
