package com.utkarsh2573.backend.doctor.controller;

import com.utkarsh2573.backend.doctor.dto.CreateDoctorRequest;
import com.utkarsh2573.backend.doctor.dto.DoctorResponse;
import com.utkarsh2573.backend.doctor.repository.DoctorRepository;
import com.utkarsh2573.backend.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorRepository repository;
    private final DoctorService doctorService;

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public List<DoctorResponse> byDepartment(@PathVariable Long departmentId) {
        return repository.findByDepartmentIdAndActiveTrue(departmentId)
                .stream()
                .map(DoctorResponse::from)
                .toList();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public List<DoctorResponse> search(@RequestParam String query) {
        return repository.searchActiveDoctors(query)
                .stream()
                .map(DoctorResponse::from)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponse create(
            @Valid @RequestBody CreateDoctorRequest request
    ) {
        return doctorService.create(request);
    }
}
