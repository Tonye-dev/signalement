package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.entity.*;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;
import com.gestion.ApplicationSignalement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebAdministrateurController {

    @Autowired private DashboardAdministrateurService dashboardService;
    @Autowired private AdministrateurService          administrateurService;
    @Autowired private VitrineMairieService           vitrineService;
    @Autowired private UtilisateurRepository          utilisateurRepository;

    private Administrateur getAdmin(Authentication auth) {
        return (Administrateur) utilisateurRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
    }

    private void common(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
    }

    // ── Dashboard ─────────────────────────────────────────────────
    @GetMapping("/administrateur/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("dashboard", dashboardService.getDashboard(admin.getMairie().getId()));
        model.addAttribute("pageTitle", "Tableau de bord");
        model.addAttribute("content", "dashboards/administrateur");
        return "fragments/layout";
    }

    // ── Profil ────────────────────────────────────────────────────
    @GetMapping("/administrateur/profil")
    public String profil(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("dashboard", dashboardService.getDashboard(admin.getMairie().getId()));
        model.addAttribute("pageTitle", "Mon profil");
        model.addAttribute("content", "profils/administrateur");
        return "fragments/layout";
    }

    @GetMapping("/administrateur/profil/modifier")
    public String modifierProfil(Authentication auth, Model model) {
        common(auth, model);
        model.addAttribute("pageTitle", "Modifier mes informations");
        model.addAttribute("content", "administrateur/modifier-profil");
        return "fragments/layout";
    }

    // ── Problèmes ─────────────────────────────────────────────────
    @GetMapping("/administrateur/problemes")
    public String problemes(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("problemes", administrateurService.voirProblemesAssignes(admin.getId()));
        model.addAttribute("pageTitle", "Signalements");
        model.addAttribute("content", "administrateur/problemes");
        return "fragments/layout";
    }

    @GetMapping("/administrateur/problemes/en-attente")
    public String problemesEnAttente(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("problemes", administrateurService.filtrerProblemes(admin.getId(), "SIGNALE", null, null));
        model.addAttribute("pageTitle", "En attente");
        model.addAttribute("content", "administrateur/problemes");
        return "fragments/layout";
    }

    @GetMapping("/administrateur/problemes/en-cours")
    public String problemesEnCours(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("problemes", administrateurService.filtrerProblemes(admin.getId(), "EN_COURS", null, null));
        model.addAttribute("pageTitle", "En cours");
        model.addAttribute("content", "administrateur/problemes");
        return "fragments/layout";
    }

    @GetMapping("/administrateur/problemes/resolus")
    public String problemesResolus(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("problemes", administrateurService.filtrerProblemes(admin.getId(), "RESOLU", null, null));
        model.addAttribute("pageTitle", "Résolus");
        model.addAttribute("content", "administrateur/problemes");
        return "fragments/layout";
    }

    // ── Rapports ──────────────────────────────────────────────────
    @GetMapping("/administrateur/rapports")
    public String rapports(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("rapports", administrateurService.voirRapportsMairie(admin.getId()));
        model.addAttribute("pageTitle", "Mes rapports");
        model.addAttribute("content", "administrateur/rapports");
        return "fragments/layout";
    }

    @GetMapping("/administrateur/rapports/ajouter")
    public String ajouterRapport(Authentication auth, Model model,
                                  @RequestParam(required = false) Long problemeId) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("problemes", administrateurService.voirProblemesAssignes(admin.getId()));
        model.addAttribute("problemeIdPreselect", problemeId);
        model.addAttribute("pageTitle", "Rédiger un rapport");
        model.addAttribute("content", "administrateur/rapport-form");
        return "fragments/layout";
    }

    // ── Annonces ──────────────────────────────────────────────────
    @GetMapping("/administrateur/annonces")
    public String annonces(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("annonces", vitrineService.getAnnonces(admin.getMairie().getId()));
        model.addAttribute("categories", CategorieAnnonce.values());
        model.addAttribute("pageTitle", "Mes annonces");
        model.addAttribute("content", "administrateur/annonces");
        return "fragments/layout";
    }

    // ── Vitrine : profil mairie ───────────────────────────────────
    @GetMapping("/administrateur/vitrine/profil")
    public String vitrineProfilMairie(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("mairie", admin.getMairie());
        model.addAttribute("pageTitle", "Profil de la mairie");
        model.addAttribute("content", "vitrine/profil");  // ← templates/vitrine/profil.html
        return "fragments/layout";
    }

    // ── Vitrine : services ────────────────────────────────────────
    @GetMapping("/administrateur/vitrine/services")
    public String vitrineServices(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("services", vitrineService.getServices(admin.getMairie().getId()));
        model.addAttribute("pageTitle", "Gestion des services");
        model.addAttribute("content", "vitrine/services-admin");  // ← templates/vitrine/services-admin.html
        return "fragments/layout";
    }

    // ── Vitrine : personnel ───────────────────────────────────────
    @GetMapping("/administrateur/vitrine/personnel")
    public String vitrinePersonnel(Authentication auth, Model model) {
        Administrateur admin = getAdmin(auth);
        model.addAttribute("currentUser", admin);
        model.addAttribute("admin", admin);
        model.addAttribute("agents", vitrineService.getAgents(admin.getMairie().getId()));
        model.addAttribute("services", vitrineService.getServices(admin.getMairie().getId()));
        model.addAttribute("pageTitle", "Gestion du personnel");
        model.addAttribute("content", "vitrine/personnel-admin");  // ← templates/vitrine/personnel-admin.html
        return "fragments/layout";
    }
}