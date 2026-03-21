package com.gestion.ApplicationSignalement.controller;

import com.gestion.dto.DashboardCitoyenDto;
import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.service.DashboardCitoyenService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/citoyen")
@PreAuthorize("hasRole('CITOYEN')")
public class DashboardCitoyenController {

    private final DashboardCitoyenService dashboardCitoyenService;

    public DashboardCitoyenController(DashboardCitoyenService dashboardCitoyenService) {
        this.dashboardCitoyenService = dashboardCitoyenService;
    }

    /**
     * 🔐 Dashboard basé sur le citoyen connecté
     * GET /api/dashboard/citoyen
     */
    @GetMapping
    public ResponseEntity<DashboardCitoyenDto> getDashboard(
            @AuthenticationPrincipal Citoyen citoyen) {

        DashboardCitoyenDto dashboard =
                dashboardCitoyenService.getDashboard(citoyen.getId());

        return ResponseEntity.ok(dashboard);
    }
}