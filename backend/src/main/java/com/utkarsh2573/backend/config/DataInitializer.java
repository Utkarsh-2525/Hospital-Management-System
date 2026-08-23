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

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
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

            Department medicine = seedDepartment(
                    "MED",
                    "General Medicine",
                    "General outpatient consultation"
            );

            Department cardiology = seedDepartment(
                    "CARD",
                    "Cardiology",
                    "Diagnosis and treatment of heart and cardiovascular conditions"
            );

            Department orthopedics = seedDepartment(
                    "ORTH",
                    "Orthopedics",
                    "Bones, joints, muscles and musculoskeletal conditions"
            );

            Department dermatology = seedDepartment(
                    "DERM",
                    "Dermatology",
                    "Diagnosis and treatment of skin, hair and nail conditions"
            );

            Department pediatrics = seedDepartment(
                    "PED",
                    "Pediatrics",
                    "Medical care for infants, children and adolescents"
            );

            Department gynecology = seedDepartment(
                    "GYN",
                    "Gynaecology",
                    "Women's reproductive and gynecological healthcare"
            );

            Department ent = seedDepartment(
                    "ENT",
                    "ENT",
                    "Ear, nose and throat healthcare"
            );

            Department neurology = seedDepartment(
                    "NEURO",
                    "Neurology",
                    "Diagnosis and treatment of neurological conditions"
            );

            Department ophthalmology = seedDepartment(
                    "OPHTH",
                    "Ophthalmology",
                    "Eye and vision healthcare"
            );

            Department surgery = seedDepartment(
                    "SURG",
                    "General Surgery",
                    "Surgical diagnosis and treatment"
            );

            Department oncology = seedDepartment(
                    "ONCO",
                    "Oncology",
                    "Diagnosis and treatment of cancer and related conditions"
            );

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
    private Department seedDepartment(
            String code,
            String name,
            String description
    ) {
        return departmentRepository.findByCode(code)
                .orElseGet(() -> departmentRepository.save(
                        Department.builder()
                                .name(name)
                                .code(code)
                                .description(description)
                                .active(true)
                                .build()
                ));
    }
}
