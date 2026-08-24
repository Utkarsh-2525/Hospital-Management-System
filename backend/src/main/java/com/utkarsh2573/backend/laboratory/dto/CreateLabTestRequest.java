package com.utkarsh2573.backend.laboratory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLabTestRequest(

        @NotBlank
        @Size(max = 40)
        String testCode,

        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 500)
        String description,

        @Size(max = 100)
        String sampleType
) {
}