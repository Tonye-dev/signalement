package com.gestion.ApplicationSignalement.controller;

import com.gestion.dto.DashboardSuperAdminDto;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;
import com.gestion.ApplicationSignalement.service.DashboardSuperAdminService;
import com.gestion.ApplicationSignalement.service.SuperAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class WebSuperAdminController {

    @Autowired private DashboardSuperAdminService dashboardSuperAdminService;
    @Autowired private SuperAdminService superAdminService;
    @Autowired private UtilisateurRepository utilisateurRepository;

    // ── Helper ───────────────────────────────────────────────────
    private void addCommonAttributes(Authentication auth, Model model) {
        utilisateurRepository.findByEmail(auth.getName())
                .ifPresent(u -> model.addAttribute("currentUser", u));
    }

    private String layout() { return "fragments/layout"; }

    // ── Dashboard ─────────────────────────────────────────────────
    @GetMapping("/superadmin/dashboard")
    public String dashboard(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        model.addAttribute("dashboard", dashboardSuperAdminService.getDashboard());
        model.addAttribute("pageTitle", "Vue d'ensemble");
        model.addAttribute("content", "dashboards/superAdmin");
        return layout();
    }

    // ── Profil ────────────────────────────────────────────────────
    @GetMapping("/superadmin/profil")
    public String profil(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        model.addAttribute("dashboard", dashboardSuperAdminService.getDashboard());
        model.addAttribute("pageTitle", "Mon profil");
        model.addAttribute("content", "profils/superAdmin");
        return layout();
    }

    // ── MAIRIES ───────────────────────────────────────────────────

    @GetMapping("/superadmin/mairies")
    public String listeMairies(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        List<Mairie> mairies = superAdminService.listerMairies();
        model.addAttribute("mairies", mairies);
        model.addAttribute("pageTitle", "Gestion des mairies");
        model.addAttribute("content", "superadmin/mairies");
        return layout();
    }

    @GetMapping("/superadmin/mairies/ajouter")
    public String ajouterMairie(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        model.addAttribute("mairie", null);
        model.addAttribute("pageTitle", "Ajouter une mairie");
        model.addAttribute("content", "superadmin/mairies-form");
        return layout();
    }

    @GetMapping("/superadmin/mairies/{id}/modifier")
    public String modifierMairie(@PathVariable Long id, Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        Mairie mairie = superAdminService.getMairieById(id);
        model.addAttribute("mairie", mairie);
        model.addAttribute("pageTitle", "Modifier la mairie");
        model.addAttribute("content", "superadmin/mairies-form");
        return layout();
    }

    // ── ADMINISTRATEURS ───────────────────────────────────────────

    @GetMapping("/superadmin/administrateurs")
    public String listeAdministrateurs(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        model.addAttribute("administrateurs", superAdminService.listerAdministrateurs());
        model.addAttribute("pageTitle", "Gestion des administrateurs");
        model.addAttribute("content", "superadmin/administrateurs");
        return layout();
    }

    @GetMapping("/superadmin/administrateurs/ajouter")
    public String ajouterAdministrateurForm(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        model.addAttribute("mairies", superAdminService.listerMairies());
        model.addAttribute("mairie", null);
        model.addAttribute("pageTitle", "Ajouter un administrateur");
        model.addAttribute("content", "superadmin/admin-form");
        return layout();
    }

    @GetMapping("/superadmin/mairies/{mairieId}/administrateurs/ajouter")
    public String ajouterAdminPourMairie(@PathVariable Long mairieId,
                                          Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        Mairie mairie = superAdminService.getMairieById(mairieId);
        model.addAttribute("mairie", mairie);
        model.addAttribute("pageTitle", "Ajouter un administrateur");
        model.addAttribute("content", "superadmin/admin-form");
        return layout();
    }

    // ── UTILISATEURS ──────────────────────────────────────────────

    @GetMapping("/superadmin/utilisateurs")
    public String listeUtilisateurs(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("pageTitle", "Tous les utilisateurs");
        model.addAttribute("content", "superadmin/utilisateurs");
        return layout();
    }

    // ── TYPES DE PROBLÈMES ────────────────────────────────────────

    @GetMapping("/superadmin/types-problemes")
    public String typesProblemes(Authentication auth, Model model) {
        addCommonAttributes(auth, model);
        model.addAttribute("typesProblemes", superAdminService.listerTypesProblemes());
        model.addAttribute("pageTitle", "Types de problèmes");
        model.addAttribute("content", "superadmin/types-problemes");
        return layout();
    }
}   
