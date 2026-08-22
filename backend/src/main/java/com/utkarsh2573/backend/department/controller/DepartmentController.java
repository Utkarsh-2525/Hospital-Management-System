package com.utkarsh2573.backend.department.controller;

import com.utkarsh2573.backend.department.entity.Department;
import com.utkarsh2573.backend.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository repository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public List<Department> getAll() {
        return repository.findAll();
    }
}
