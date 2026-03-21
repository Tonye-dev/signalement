package com.gestion.ApplicationSignalement.controller;

import com.gestion.dto.DashboardSuperAdminDto;
import com.gestion.ApplicationSignalement.service.DashboardSuperAdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/superadmin")
@PreAuthorize("hasRole('SUPERADMIN')")
public class DashboardSuperAdminController {

    private final DashboardSuperAdminService dashboardSuperAdminService;

    public DashboardSuperAdminController(DashboardSuperAdminService dashboardSuperAdminService) {
        this.dashboardSuperAdminService = dashboardSuperAdminService;
    }

    /**
     * 🔐 Récupérer le dashboard du SUPERADMIN
     * Endpoint : GET /api/dashboard/superadmin
     */
    @GetMapping
    public ResponseEntity<DashboardSuperAdminDto> getDashboard() {

        DashboardSuperAdminDto dashboard =
                dashboardSuperAdminService.getDashboard();

        return ResponseEntity.ok(dashboard);
    }
}