package com.gestion.ApplicationSignalement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.service.SuperAdminService;

@RestController
@RequestMapping("/api/superadmin")
@PreAuthorize("hasRole('SUPERADMIN')")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    // 🔹 Constructeur manuel (remplace Lombok)
    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    // Ajouter une mairie
    @PostMapping("/mairies")
    public ResponseEntity<Mairie> ajouterMairie(@RequestBody Mairie mairie) {
        return ResponseEntity.ok(superAdminService.ajouterMairie(mairie));
    }

    // Modifier une mairie
    @PutMapping("/mairies/{id}")
    public ResponseEntity<Mairie> modifierMairie(@PathVariable Long id,
                                                 @RequestBody Mairie mairieDetails) {
        return ResponseEntity.ok(superAdminService.modifierMairie(id, mairieDetails));
    }

    // Ajouter un administrateur à une mairie
    @PostMapping("/mairies/{mairieId}/administrateurs")
    public ResponseEntity<Administrateur> ajouterAdministrateur(
            @PathVariable Long mairieId,
            @RequestBody Administrateur administrateur) {

        return ResponseEntity.ok(
                superAdminService.ajouterAdministrateur(administrateur, mairieId)
        );
    }

    // Créer un type de problème
    @PostMapping("/types-problemes")
    public ResponseEntity<TypeProbleme> creerTypeProbleme(
            @RequestParam String nomType) {

        return ResponseEntity.ok(
                superAdminService.creerTypeProbleme(nomType)
        );
    }
}