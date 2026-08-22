package com.utkarsh2573.backend.medicine.dto;

import com.utkarsh2573.backend.medicine.entity.Medicine;

public record MedicineResponse(
        Long id,
        String medicineCode,
        String name,
        String genericName,
        String dosageForm,
        String strength
) {

    public static MedicineResponse from(Medicine m) {
        return new MedicineResponse(
                m.getId(),
                m.getMedicineCode(),
                m.getName(),
                m.getGenericName(),
                m.getDosageForm(),
                m.getStrength()
        );
    }
}