package com.utkarsh2573.backend.laboratory.controller;

import com.utkarsh2573.backend.laboratory.dto.CreateLabResultRequest;
import com.utkarsh2573.backend.laboratory.dto.LabResultResponse;
import com.utkarsh2573.backend.laboratory.service.LabResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/laboratory/results")
@RequiredArgsConstructor
public class LabResultController {

    private final LabResultService labResultService;

    @PostMapping("/items/{orderItemId}")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public LabResultResponse create(
            @PathVariable Long orderItemId,
            @Valid @RequestBody CreateLabResultRequest request
    ) {
        return labResultService.create(
                orderItemId,
                request
        );
    }

    @GetMapping("/items/{orderItemId}")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN','ADMIN','DOCTOR','PATIENT')")
    public LabResultResponse get(
            @PathVariable Long orderItemId
    ) {
        return labResultService.getByOrderItemId(
                orderItemId
        );
    }
}