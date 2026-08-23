package com.utkarsh2573.backend.config;

import com.utkarsh2573.backend.common.enums.Role;
import com.utkarsh2573.backend.department.entity.Department;
import com.utkarsh2573.backend.department.repository.DepartmentRepository;
import com.utkarsh2573.backend.doctor.entity.Doctor;
import com.utkarsh2573.backend.doctor.repository.DoctorRepository;
import com.utkarsh2573.backend.laboratory.entity.LabTest;
import com.utkarsh2573.backend.laboratory.repository.LabTestRepository;
import com.utkarsh2573.backend.medicine.entity.Medicine;
import com.utkarsh2573.backend.medicine.repository.MedicineRepository;
import com.utkarsh2573.backend.user.entity.User;
import com.utkarsh2573.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final MedicineRepository medicineRepository;
    private final LabTestRepository labTestRepository;

    @Bean
    CommandLineRunner seedData() {
        return args -> {
            createIfMissing("admin", "admin@hms.local", "Admin@123", Role.ADMIN);
            createIfMissing("reception", "reception@hms.local", "Reception@123", Role.RECEPTIONIST);
            User doctorUser = createIfMissing("doctor", "doctor@hms.local", "Doctor@123", Role.DOCTOR);
            createIfMissing("pharmacy", "pharmacy@hms.local", "Pharmacy@123", Role.PHARMACIST);
            createIfMissing("lab", "lab@hms.local", "Lab@123", Role.LAB_TECHNICIAN);
            createIfMissing("patient", "patient@hms.local", "Patient@123", Role.PATIENT);

            Department medicine = departmentRepository.findByCode("MED")
                    .orElseGet(() -> departmentRepository.save(
                            Department.builder()
                                    .name("General Medicine")
                                    .code("MED")
                                    .description("General outpatient consultation")
                                    .active(true)
                                    .build()
                    ));

            if (!doctorRepository.existsByDoctorNumber("D-0001")) {
                doctorRepository.save(
                        Doctor.builder()
                                .doctorNumber("D-0001")
                                .user(doctorUser)
                                .fullName("Dr. Demo")
                                .qualification("MBBS")
                                .specialization("General Medicine")
                                .department(medicine)
                                .consultationFee(new BigDecimal("500.00"))
                                .active(true)
                                .build()
                );
            }

//            if (!doctorRepository.existsByDoctorNumber("D-0002")) {
//                doctorRepository.save(
//                        Doctor.builder()
//                                .doctorNumber("D-0002")
//                                .user(doctorUser)
//                                .fullName("Dr. Vansh")
//                                .qualification("MBBS, MS, MD, DNB")
//                                .specialization("Gynaecology")
//                                .department(medicine)
//                                .consultationFee(new BigDecimal("1500.00"))
//                                .active(true)
//                                .build()
//                );
//            }

            seedMedicine("MED-001", "Paracetamol 650 mg", "Paracetamol", "Tablet", "650 mg");
            seedMedicine("MED-002", "Cetirizine 10 mg", "Cetirizine", "Tablet", "10 mg");
            seedMedicine("MED-003", "Omeprazole 20 mg", "Omeprazole", "Capsule", "20 mg");

            seedLabTest("CBC", "Complete Blood Count", "Blood analysis", "Blood");
            seedLabTest("RFT", "Renal Function Test", "Kidney function panel", "Blood");
            seedLabTest("LFT", "Liver Function Test", "Liver function panel", "Blood");
            seedLabTest("URINE-R", "Urine Routine", "Routine urine examination", "Urine");
        };
    }

    private User createIfMissing(
            String username,
            String email,
            String password,
            Role role
    ) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .username(username)
                                .email(email)
                                .password(passwordEncoder.encode(password))
                                .role(role)
                                .enabled(true)
                                .build()
                ));
    }

    private void seedMedicine(
            String code,
            String name,
            String generic,
            String form,
            String strength
    ) {
        if (!medicineRepository.existsByMedicineCode(code)) {
            medicineRepository.save(
                    Medicine.builder()
                            .medicineCode(code)
                            .name(name)
                            .genericName(generic)
                            .dosageForm(form)
                            .strength(strength)
                            .active(true)
                            .build()
            );
        }
    }

    private void seedLabTest(
            String code,
            String name,
            String description,
            String sampleType
    ) {
        if (!labTestRepository.existsByTestCode(code)) {
            labTestRepository.save(
                    LabTest.builder()
                            .testCode(code)
                            .name(name)
                            .description(description)
                            .sampleType(sampleType)
                            .active(true)
                            .build()
            );
        }
    }
}
