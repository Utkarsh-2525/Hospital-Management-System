package com.utkarsh2573.backend.doctor.service;

import com.utkarsh2573.backend.common.enums.Role;
import com.utkarsh2573.backend.department.entity.Department;
import com.utkarsh2573.backend.department.repository.DepartmentRepository;
import com.utkarsh2573.backend.doctor.dto.CreateDoctorRequest;
import com.utkarsh2573.backend.doctor.dto.DoctorResponse;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.doctor.repository.DoctorRepository;
import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.user.entity.User;
import com.utkarsh2573.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private String generateDoctorNumber() {

        String doctorNumber;

        do {
            doctorNumber = "D-" +
                    String.format(
                            "%04d",
                            (int) (Math.random() * 10000)
                    );

        } while (doctorRepository.existsByDoctorNumber(doctorNumber));

        return doctorNumber;
    }

    @Transactional
    public DoctorResponse create(CreateDoctorRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException(
                    "Username already exists"
            );
        }

        Department department =
                departmentRepository.findById(request.departmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found: "
                                                + request.departmentId()
                                )
                        );

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.DOCTOR)
                .enabled(true)
                .build();

        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .doctorNumber(generateDoctorNumber())
                .user(user)
                .fullName(request.fullName())
                .specialization(request.specialization())
                .department(department)
                .consultationFee(request.consultationFee())
                .active(true)
                .build();

        return DoctorResponse.from(
                doctorRepository.save(doctor)
        );
    }
}