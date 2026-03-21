package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.entity.*;
import com.gestion.ApplicationSignalement.service.VitrineMairieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AnnonceController {

    @Autowired private VitrineMairieService vitrineService;

    // ── Publier une annonce ───────────────────────────────────────
    @PostMapping("/administrateur/annonces")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Annonce> publier(
            @RequestParam Long administrateurId,
            @RequestParam String titre,
            @RequestParam String contenu,
            @RequestParam CategorieAnnonce categorie,
            @RequestParam(defaultValue = "false") boolean estArrete) {
        return ResponseEntity.ok(
                vitrineService.publierAnnonce(administrateurId, titre, contenu, categorie, estArrete));
    }

    // ── Supprimer une annonce ─────────────────────────────────────
    @DeleteMapping("/administrateur/annonces/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long id,
            @RequestParam Long administrateurId) {
        vitrineService.supprimerAnnonce(id, administrateurId);
        return ResponseEntity.noContent().build();
    }

    // ── Notifications in-app (panneau cloche) ─────────────────────
    @GetMapping("/citoyen/notifications")
    @PreAuthorize("hasRole('CITOYEN')")
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long citoyenId) {
        return ResponseEntity.ok(vitrineService.getNotifications(citoyenId));
    }

    @GetMapping("/citoyen/notifications/count")
    @PreAuthorize("hasRole('CITOYEN')")
    public ResponseEntity<Long> countNonLues(@RequestParam Long citoyenId) {
        return ResponseEntity.ok(vitrineService.getNbNonLues(citoyenId));
    }

    @PostMapping("/citoyen/notifications/lues")
    @PreAuthorize("hasRole('CITOYEN')")
    public ResponseEntity<Void> marquerLues(@RequestParam Long citoyenId) {
        vitrineService.marquerToutesLues(citoyenId);
        return ResponseEntity.ok().build();
    }

    // ── Préférences notifications ──────────────────────────────────
    @PostMapping("/citoyen/preferences")
    @PreAuthorize("hasRole('CITOYEN')")
    public ResponseEntity<PreferenceNotification> sauvegarderPreferences(
            @RequestParam Long citoyenId,
            @RequestParam(required = false) Set<CategorieAnnonce> categoriesEmail,
            @RequestParam(required = false) Set<CategorieAnnonce> categoriesSms) {
        return ResponseEntity.ok(
                vitrineService.sauvegarderPreferences(citoyenId, categoriesEmail, categoriesSms));
    }

    // ── Services vitrine admin ─────────────────────────────────────
    @PostMapping("/administrateur/vitrine/services")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<ServiceMunicipal> ajouterService(
            @RequestParam Long adminId,
            @RequestParam String nom,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String icone) {
        return ResponseEntity.ok(vitrineService.ajouterService(adminId, nom, description, icone));
    }

    // ── Profil mairie ─────────────────────────────────────────────
    @PostMapping(value = "/administrateur/vitrine/profil", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Mairie> mettreAJourProfil(
            @RequestParam Long adminId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String emailOfficiel,
            @RequestParam(required = false) String horaires,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile photo) throws Exception {
        return ResponseEntity.ok(
                vitrineService.mettreAJourProfil(adminId, description, telephone, emailOfficiel, horaires, photo));
    }

    // ── Personnel ─────────────────────────────────────────────────
    @PostMapping(value = "/administrateur/vitrine/personnel", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<AgentMunicipal> ajouterAgent(
            @RequestParam Long adminId,
            @RequestParam String prenom,
            @RequestParam String nom,
            @RequestParam String poste,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) String dateNaissance,
            @RequestParam(required = false) String datePriseDeFonction,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile photo) throws Exception {
        AgentMunicipal agent = new AgentMunicipal();
        agent.setPrenom(prenom);
        agent.setNom(nom);
        agent.setPoste(poste);
        if (dateNaissance != null && !dateNaissance.isBlank())
            agent.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
        if (datePriseDeFonction != null && !datePriseDeFonction.isBlank())
            agent.setDatePriseDeFonction(java.time.LocalDate.parse(datePriseDeFonction));
        return ResponseEntity.ok(vitrineService.ajouterAgent(adminId, agent, serviceId, photo));
    }

    @DeleteMapping("/administrateur/vitrine/personnel/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> supprimerAgent(@PathVariable Long id) {
        vitrineService.supprimerAgent(id);
        return ResponseEntity.noContent().build();
    }
}