package com.utkarsh2573.backend.laboratory.repository;

import com.utkarsh2573.backend.laboratory.entity.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabOrderRepository
        extends JpaRepository<LabOrder, Long> {

    Optional<LabOrder> findByOrderNumber(String orderNumber);

    List<LabOrder> findByPatientIdOrderByOrderedAtDesc(
            Long patientId
    );

//    List<LabOrder> findByConsultationIdOrderByOrderedAtDesc(
//            Long consultationId
//    );

    boolean existsByOrderNumber(String orderNumber);
}