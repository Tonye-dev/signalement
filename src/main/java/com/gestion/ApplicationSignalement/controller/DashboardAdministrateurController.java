package com.gestion.ApplicationSignalement.controller;

import com.gestion.dto.DashboardAdministrateurDto;
import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.service.DashboardAdministrateurService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/administrateur")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class DashboardAdministrateurController {

    private final DashboardAdministrateurService dashboardAdministrateurService;

    public DashboardAdministrateurController(
            DashboardAdministrateurService dashboardAdministrateurService) {
        this.dashboardAdministrateurService = dashboardAdministrateurService;
    }

    /**
     * 🔐 Dashboard basé sur la mairie de l'administrateur connecté
     * GET /api/dashboard/administrateur
     */
    @GetMapping
    public ResponseEntity<DashboardAdministrateurDto> getDashboard(
            @AuthenticationPrincipal Administrateur administrateur) {

        DashboardAdministrateurDto dashboard =
                dashboardAdministrateurService
                        .getDashboard(administrateur.getMairie().getId());

        return ResponseEntity.ok(dashboard);
    }
}