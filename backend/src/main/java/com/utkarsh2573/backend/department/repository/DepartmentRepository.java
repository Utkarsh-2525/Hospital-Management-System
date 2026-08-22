package com.utkarsh2573.backend.department.repository;

import com.utkarsh2573.backend.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByCode(String code);
}