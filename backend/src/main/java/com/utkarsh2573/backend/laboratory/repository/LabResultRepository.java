package com.utkarsh2573.backend.laboratory.repository;

import com.utkarsh2573.backend.laboratory.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabResultRepository
        extends JpaRepository<LabResult, Long> {

    Optional<LabResult> findByLabOrderItemId(Long labOrderItemId);

    boolean existsByLabOrderItemId(Long labOrderItemId);
}