package com.gestion.ApplicationSignalement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.gestion.ApplicationSignalement.config.jwt.JwtAuthenticationFilter;
import com.gestion.ApplicationSignalement.security.CustomLoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

        // désactiver CSRF pour API
        .csrf(csrf -> csrf.disable())

        // gestion session (nécessaire pour Thymeleaf login)
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )

        // autorisations
        .authorizeHttpRequests(auth -> auth

            // pages publiques
            .requestMatchers(
                "/login",
                "/home",
                "/register",
                "/activation",
                "/css/**",
                "/js/**",
                "/images/**",
                "/uploads/**",
                "/api/auth/**",
                "/api/verification/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/error"
            ).permitAll()

            // vitrine mairies (connexion requise)
            .requestMatchers("/mairies/**").authenticated()

            // dashboards par rôle
            .requestMatchers("/citoyen/**").hasRole("CITOYEN")
            .requestMatchers("/administrateur/**").hasRole("ADMINISTRATEUR")
            .requestMatchers("/superadmin/**").hasRole("SUPERADMIN")
            .requestMatchers("/api/pdf/superadmin/**").hasRole("SUPERADMIN")
            .requestMatchers("/api/pdf/**").hasRole("ADMINISTRATEUR")
            .requestMatchers("/api/citoyen/**").hasRole("CITOYEN")
            .requestMatchers("/api/administrateur/**").hasRole("ADMINISTRATEUR")

            // le reste nécessite connexion
            .anyRequest().authenticated()
        )

        // login formulaire (page login.html)
        .formLogin(form -> form
            .loginPage("/login")
            .successHandler(customLoginSuccessHandler)
            .permitAll()
        )

        // logout
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        )

        // filtre JWT pour API
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    // AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // encodeur mot de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}