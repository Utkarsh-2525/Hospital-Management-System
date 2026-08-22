package com.utkarsh2573.backend.consultation.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record VitalRequest(
        @DecimalMin("0") BigDecimal temperatureC,
        @DecimalMin("0") BigDecimal systolicBp,
        @DecimalMin("0") BigDecimal diastolicBp,
        @DecimalMin("0") BigDecimal pulseBpm,
        @DecimalMin("0") BigDecimal respiratoryRate,
        @DecimalMin("0") BigDecimal oxygenSaturation,
        @DecimalMin("0") BigDecimal weightKg,
        @DecimalMin("0") BigDecimal heightCm
) {}
