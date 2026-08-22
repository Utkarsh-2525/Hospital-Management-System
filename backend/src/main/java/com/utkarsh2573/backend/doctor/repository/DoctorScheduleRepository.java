package com.utkarsh2573.backend.doctor.repository;

import com.utkarsh2573.backend.doctor.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorIdAndAvailableTrueOrderByDayOfWeekAscStartTimeAsc(Long doctorId);

    List<DoctorSchedule> findByDoctorIdAndDayOfWeekAndAvailableTrue(
            Long doctorId, DayOfWeek dayOfWeek);
}
