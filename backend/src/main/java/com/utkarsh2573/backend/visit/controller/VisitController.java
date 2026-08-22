package com.utkarsh2573.backend.visit.controller;

import com.utkarsh2573.backend.visit.dto.*;
import com.utkarsh2573.backend.visit.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<VisitResponse> create(
            @Valid @RequestBody CreateVisitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(visitService.createVisit(request));
    }

    @PostMapping("/{visitId}/queue")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<QueueResponse> generateQueue(@PathVariable Long visitId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(visitService.generateQueue(visitId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR')")
    public VisitResponse get(@PathVariable Long id) {
        return visitService.getVisit(id);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','PATIENT')")
    public List<VisitResponse> patientHistory(@PathVariable Long patientId) {
        return visitService.patientHistory(patientId);
    }

    @GetMapping("/doctor/{doctorId}/queue")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public List<QueueResponse> doctorQueue(
            @PathVariable Long doctorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return visitService.doctorQueue(
                doctorId,
                date == null ? LocalDate.now() : date
        );
    }
}
