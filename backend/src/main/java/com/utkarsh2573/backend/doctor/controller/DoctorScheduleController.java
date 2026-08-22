package com.utkarsh2573.backend.doctor.controller;

import com.utkarsh2573.backend.doctor.dto.DoctorScheduleRequest;
import com.utkarsh2573.backend.doctor.dto.DoctorScheduleResponse;
import com.utkarsh2573.backend.doctor.service.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @PostMapping("/{doctorId}/schedule")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public DoctorScheduleResponse createOrUpdate(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorScheduleRequest request
    ) {
        return scheduleService.createOrUpdate(doctorId, request);
    }

    @GetMapping("/{doctorId}/schedule")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST','PATIENT')")
    public List<DoctorScheduleResponse> getSchedule(
            @PathVariable Long doctorId
    ) {
        return scheduleService.getDoctorSchedule(doctorId);
    }
}
