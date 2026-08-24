package com.utkarsh2573.backend.laboratory.repository;

import com.utkarsh2573.backend.laboratory.entity.LabOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabOrderItemRepository
        extends JpaRepository<LabOrderItem, Long> {
}