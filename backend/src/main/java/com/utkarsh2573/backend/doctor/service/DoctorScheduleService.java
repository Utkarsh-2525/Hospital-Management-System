package com.utkarsh2573.backend.doctor.service;

import com.utkarsh2573.backend.doctor.dto.*;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.doctor.entity.DoctorSchedule;
import com.utkarsh2573.backend.doctor.repository.DoctorRepository;
import com.utkarsh2573.backend.doctor.repository.DoctorScheduleRepository;
import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;

    @Transactional
    public DoctorScheduleResponse createOrUpdate(Long doctorId, DoctorScheduleRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BadRequestException("End time must be after start time");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found: " + doctorId));

        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndDayOfWeekAndAvailableTrue(
                        doctorId, request.dayOfWeek())
                .stream()
                .findFirst()
                .orElseGet(() -> DoctorSchedule.builder()
                        .doctor(doctor)
                        .dayOfWeek(request.dayOfWeek())
                        .build());

        schedule.setStartTime(request.startTime());
        schedule.setEndTime(request.endTime());
        schedule.setAvailable(request.available());

        return DoctorScheduleResponse.from(scheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> getDoctorSchedule(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found: " + doctorId);
        }

        return scheduleRepository
                .findByDoctorIdAndAvailableTrueOrderByDayOfWeekAscStartTimeAsc(doctorId)
                .stream()
                .map(DoctorScheduleResponse::from)
                .toList();
    }
}
