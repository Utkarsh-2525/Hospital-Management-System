package com.utkarsh2573.backend.doctor.repository;

import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.visit.dto.VisitResponse;
import com.utkarsh2573.backend.visit.service.VisitService;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByDoctorNumber(String doctorNumber);
    Optional<Doctor> findByUserUsername(String username);
    boolean existsByDoctorNumber(String doctorNumber);

    @EntityGraph(attributePaths = {"department"})
    List<Doctor> findByDepartmentIdAndActiveTrue(Long departmentId);

    @EntityGraph(attributePaths = {"department"})
    @Query("""
            SELECT d
            FROM Doctor d
            WHERE d.active = true
            AND (
                LOWER(d.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(d.doctorNumber) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            """)
    List<Doctor> searchActiveDoctors(@Param("query") String query);
}