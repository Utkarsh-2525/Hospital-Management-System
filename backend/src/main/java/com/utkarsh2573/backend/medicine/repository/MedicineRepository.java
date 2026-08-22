package com.utkarsh2573.backend.medicine.repository;

import com.utkarsh2573.backend.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    boolean existsByMedicineCode(String medicineCode);

    List<Medicine> findByActiveTrueAndNameContainingIgnoreCase(String name);
}