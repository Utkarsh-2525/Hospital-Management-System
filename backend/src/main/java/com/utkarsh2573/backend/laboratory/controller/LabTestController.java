package com.utkarsh2573.backend.laboratory.controller;

import com.utkarsh2573.backend.laboratory.dto.CreateLabTestRequest;
import com.utkarsh2573.backend.laboratory.dto.LabTestResponse;
import com.utkarsh2573.backend.laboratory.entity.LabTest;
import com.utkarsh2573.backend.laboratory.repository.LabTestRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab/tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestRepository labTestRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LabTestResponse> create(
            @Valid @RequestBody CreateLabTestRequest request
    ) {

        if (labTestRepository.existsByTestCode(request.testCode())) {
            throw new IllegalArgumentException(
                    "Lab test code already exists: " + request.testCode()
            );
        }

        LabTest labTest = LabTest.builder()
                .testCode(request.testCode())
                .name(request.name())
                .description(request.description())
                .sampleType(request.sampleType())
                .active(true)
                .build();

        LabTest saved = labTestRepository.save(labTest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(LabTestResponse.from(saved));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','LAB_TECHNICIAN')")
    public List<LabTestResponse> search(
            @RequestParam String query
    ) {
        return labTestRepository
                .findByActiveTrueAndNameContainingIgnoreCase(query)
                .stream()
                .map(LabTestResponse::from)
                .toList();
    }
}