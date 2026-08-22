package com.utkarsh2573.backend.laboratory.controller;

import com.utkarsh2573.backend.laboratory.dto.*;
import com.utkarsh2573.backend.laboratory.service.LabRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab/recommendations")
@RequiredArgsConstructor
public class LabRecommendationController {

    private final LabRecommendationService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public LabRecommendationResponse recommend(
            @Valid @RequestBody LabRecommendationRequest request,
            Authentication authentication) {
        return service.recommend(request, authentication.getName());
    }

    @GetMapping("/consultation/{consultationId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','LAB_TECHNICIAN','PATIENT')")
    public List<LabRecommendationResponse> list(
            @PathVariable Long consultationId) {
        return service.list(consultationId);
    }
}
