package com.utkarsh2573.backend.medicine.controller;

import com.utkarsh2573.backend.medicine.dto.MedicineResponse;
import com.utkarsh2573.backend.medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineRepository medicineRepository;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PHARMACIST')")
    public List<MedicineResponse> search(
            @RequestParam String query) {

        return medicineRepository
                .findByActiveTrueAndNameContainingIgnoreCase(query)
                .stream()
                .map(MedicineResponse::from)
                .toList();
    }
}