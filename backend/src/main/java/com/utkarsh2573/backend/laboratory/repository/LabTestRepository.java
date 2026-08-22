package com.utkarsh2573.backend.laboratory.repository;

import com.utkarsh2573.backend.laboratory.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    boolean existsByTestCode(String testCode);

    List<LabTest> findByActiveTrueAndNameContainingIgnoreCase(String name);
}