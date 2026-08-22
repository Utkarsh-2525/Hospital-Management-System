package com.utkarsh2573.backend.laboratory.controller;

import com.utkarsh2573.backend.laboratory.dto.CreateLabOrderRequest;
import com.utkarsh2573.backend.laboratory.dto.LabOrderResponse;
import com.utkarsh2573.backend.laboratory.service.LabOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/laboratory/orders")
@RequiredArgsConstructor
public class LabOrderController {

    private final LabOrderService labOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR','LAB_TECHNICIAN')")
    public LabOrderResponse create(
            @Valid @RequestBody CreateLabOrderRequest request
    ) {
        return labOrderService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN','ADMIN')")
    public List<LabOrderResponse> getAll() {
        return labOrderService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN','ADMIN')")
    public LabOrderResponse getById(
            @PathVariable Long id
    ) {
        return labOrderService.getById(id);
    }

    @PatchMapping("/{id}/sample")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public LabOrderResponse collectSample(
            @PathVariable Long id
    ) {
        return labOrderService.collectSample(id);
    }

    @PatchMapping("/{id}/processing")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public LabOrderResponse startProcessing(
            @PathVariable Long id
    ) {
        return labOrderService.startProcessing(id);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public LabOrderResponse complete(
            @PathVariable Long id
    ) {
        return labOrderService.complete(id);
    }
}