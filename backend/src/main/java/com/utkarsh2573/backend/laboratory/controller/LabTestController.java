package com.utkarsh2573.backend.laboratory.controller;

import com.utkarsh2573.backend.laboratory.dto.LabTestResponse;
import com.utkarsh2573.backend.laboratory.repository.LabTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lab/tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestRepository labTestRepository;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','LAB_TECHNICIAN')")
    public List<LabTestResponse> search(@RequestParam String query) {
        return labTestRepository.findByActiveTrueAndNameContainingIgnoreCase(query)
                .stream()
                .map(LabTestResponse::from)
                .toList();
    }
}
