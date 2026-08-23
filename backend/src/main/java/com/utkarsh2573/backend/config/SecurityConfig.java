package com.utkarsh2573.backend.config;

import com.utkarsh2573.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/actuator/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Admin
                        .requestMatchers("/api/v1/admin/**")
                        .hasRole("ADMIN")

                        // Receptionist
                        .requestMatchers("/api/v1/receptionist/**")
                        .hasAnyRole(
                                "ADMIN",
                                "RECEPTIONIST"
                        )

                        // Doctor
                        .requestMatchers("/api/v1/doctor/**")
                        .hasAnyRole(
                                "ADMIN",
                                "DOCTOR"
                        )

                        // Pharmacy
                        .requestMatchers("/api/v1/pharmacy/**")
                        .hasAnyRole(
                                "ADMIN",
                                "PHARMACIST"
                        )

                        // Laboratory
                        .requestMatchers("/api/v1/lab/**")
                        .hasAnyRole(
                                "ADMIN",
                                "LAB_TECHNICIAN"
                        )

                        // Patient
                        .requestMatchers("/api/v1/patient/**")
                        .hasAnyRole(
                                "ADMIN",
                                "PATIENT"
                        )

                        // Doctor management
                        // Only Admin can create doctors
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/doctors"
                        )
                        .hasRole("ADMIN")

                        // Doctor search/list
                        // Receptionist needs this for visit creation
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/doctors/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECEPTIONIST",
                                "PATIENT"
                        )

                        // Everything else
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}