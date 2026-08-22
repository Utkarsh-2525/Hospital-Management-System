package com.utkarsh2573.backend.pharmacy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateDispenseRequest(
        @NotNull Long prescriptionId,
        @Size(max = 1000) String pharmacistNotes,
        @Valid List<Item> items
) {
    public record Item(
            @NotNull Long medicineId,
            @NotNull Integer dispensedQuantity,
            @Size(max = 500) String notes
    ) {}
}
