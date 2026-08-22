package com.utkarsh2573.backend.patient.service;

import com.utkarsh2573.backend.common.enums.Role;
import com.utkarsh2573.backend.exception.BadRequestException;
import com.utkarsh2573.backend.exception.ResourceNotFoundException;
import com.utkarsh2573.backend.patient.dto.*;
import com.utkarsh2573.backend.patient.entity.Patient;
import com.utkarsh2573.backend.patient.repository.PatientRepository;
import com.utkarsh2573.backend.user.entity.User;
import com.utkarsh2573.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String PASSWORD_CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789@#$";

    @Transactional
    public PatientCreationResponse create(CreatePatientRequest request) {

        // ---------------------------------------------------------
        // 1. Patient duplicate checks
        // ---------------------------------------------------------

        if (patientRepository.existsByPhone(request.phone())) {
            throw new BadRequestException(
                    "Patient with phone number "
                            + request.phone()
                            + " already exists"
            );
        }

        if (request.email() != null
                && !request.email().isBlank()
                && patientRepository.existsByEmail(request.email())) {

            throw new BadRequestException(
                    "Patient with email "
                            + request.email()
                            + " already exists"
            );
        }

        // ---------------------------------------------------------
        // 2. Generate patient number
        // ---------------------------------------------------------

        String patientNumber = generatePatientNumber();

        // ---------------------------------------------------------
        // 3. Generate patient login
        // ---------------------------------------------------------

        String username = patientNumber;

        String temporaryPassword = generateTemporaryPassword();

        String userEmail;

        if (request.email() != null && !request.email().isBlank()) {

            userEmail = request.email().trim().toLowerCase();

            if (userRepository.existsByEmail(userEmail)) {
                throw new BadRequestException(
                        "Email " + userEmail
                                + " is already associated with another user account"
                );
            }

        } else {

            /*
             * User.email is NOT nullable in the current User entity.
             * Therefore patients without an email need a generated
             * internal email address.
             */
            userEmail = patientNumber.toLowerCase()
                    + "@patient.hms.local";
        }

        // ---------------------------------------------------------
        // 4. Create User
        // ---------------------------------------------------------

        User user = User.builder()
                .username(username)
                .email(userEmail)
                .password(passwordEncoder.encode(temporaryPassword))
                .role(Role.PATIENT)
                .enabled(true)
                .build();

        userRepository.save(user);

        // ---------------------------------------------------------
        // 5. Create Patient
        // ---------------------------------------------------------

        Patient patient = Patient.builder()
                .patientNumber(patientNumber)
                .fullName(request.fullName())
                .dateOfBirth(request.dateOfBirth())
                .gender(request.gender())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .bloodGroup(request.bloodGroup())
                .allergies(request.allergies())
                .emergencyContactName(request.emergencyContactName())
                .emergencyContactPhone(request.emergencyContactPhone())
                .user(user)
                .build();

        patient = patientRepository.save(patient);

        // ---------------------------------------------------------
        // 6. Return patient + credentials
        // ---------------------------------------------------------

        return new PatientCreationResponse(
                PatientResponse.from(patient),
                username,
                temporaryPassword
        );
    }

    @Transactional(readOnly = true)
    public PatientResponse get(Long id) {

        return PatientResponse.from(
                patientRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found: " + id
                                ))
        );
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> search(
            String query,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("fullName").ascending()
        );

        Page<Patient> patients =
                query == null || query.isBlank()
                        ? patientRepository.findAll(pageable)
                        : patientRepository
                        .findByFullNameContainingIgnoreCaseOrPhoneContaining(
                                query,
                                query,
                                pageable
                        );

        return patients.map(PatientResponse::from);
    }

    private String generatePatientNumber() {

        String number;

        do {
            number = "P-" +
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 8)
                            .toUpperCase();

        } while (patientRepository.existsByPatientNumber(number));

        return number;
    }

    private String generateTemporaryPassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {

            int index = RANDOM.nextInt(
                    PASSWORD_CHARACTERS.length()
            );

            password.append(
                    PASSWORD_CHARACTERS.charAt(index)
            );
        }

        return password.toString();
    }
}