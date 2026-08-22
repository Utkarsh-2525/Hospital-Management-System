package com.utkarsh2573.backend.doctor.repository;

import com.utkarsh2573.backend.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorNumber(String doctorNumber);

    List<Doctor> findByDepartmentIdAndActiveTrue(Long departmentId);

    boolean existsByDoctorNumber(String doctorNumber);

    @Query("""
        select d from Doctor d
        where d.active = true
        and (
            lower(d.fullName) like lower(concat('%', :search, '%'))
            or lower(d.specialization) like lower(concat('%', :search, '%'))
        )
    """)
    List<Doctor> searchActiveDoctors(@Param("search") String search);
}