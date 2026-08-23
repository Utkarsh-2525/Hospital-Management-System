package com.utkarsh2573.backend.visit.repository;

import com.utkarsh2573.backend.visit.entity.Visit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<Visit> findByVisitNumber(String visitNumber);

    List<Visit> findByPatientIdOrderByVisitDateDescCreatedAtDesc(
            Long patientId
    );

    List<Visit> findByDoctorIdOrderByVisitDateDescCreatedAtDesc(
            Long doctorId
    );

    List<Visit> findByDoctorIdAndVisitDateOrderByCreatedAtAsc(
            Long doctorId,
            LocalDate visitDate
    );

    long countByDoctorIdAndVisitDate(
            Long doctorId,
            LocalDate visitDate
    );

    boolean existsByVisitNumber(String visitNumber);

    Page<Visit> findByPatientId(
            Long patientId,
            Pageable pageable
    );

    boolean existsByPatientIdAndIdLessThan(
            Long patientId,
            Long visitId
    );
}