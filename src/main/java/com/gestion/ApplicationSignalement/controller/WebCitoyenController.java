package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.entity.*;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;
import com.gestion.ApplicationSignalement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebCitoyenController {

    @Autowired private DashboardCitoyenService dashboardService;
    @Autowired private CitoyenService          citoyenService;
    @Autowired private VitrineMairieService    vitrineService;
    @Autowired private TypeProblemeService     typeProblemeService;
    @Autowired private UtilisateurRepository   utilisateurRepository;

    private Citoyen getCitoyen(Authentication auth) {
        return (Citoyen) utilisateurRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));
    }

    // Récupère le nb de notifs sans planter si aucune préférence encore créée
    private long getNbNotifs(Long citoyenId) {
        try {
            return vitrineService.getNbNonLues(citoyenId);
        } catch (Exception e) {
            return 0;
        }
    }

    private void common(Citoyen citoyen, Model model) {
        model.addAttribute("currentUser", citoyen);
        model.addAttribute("citoyen", citoyen);
        model.addAttribute("nbNotifications", getNbNotifs(citoyen.getId()));
    }

    // ── Dashboard ─────────────────────────────────────────────────
    @GetMapping("/citoyen/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("dashboard", dashboardService.getDashboard(citoyen.getId()));
        model.addAttribute("pageTitle", "Tableau de bord");
        model.addAttribute("content", "dashboards/citoyen");
        return "fragments/layout";
    }

    // ── Profil ────────────────────────────────────────────────────
    @GetMapping("/citoyen/profil")
    public String profil(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("dashboard", dashboardService.getDashboard(citoyen.getId()));
        model.addAttribute("pageTitle", "Mon profil");
        model.addAttribute("content", "profils/citoyen");
        return "fragments/layout";
    }

    // ── Modifier profil ───────────────────────────────────────────
    @GetMapping("/citoyen/profil/modifier")
    public String modifierProfil(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("pageTitle", "Modifier mes informations");
        model.addAttribute("content", "citoyen/modifier-profil");
        return "fragments/layout";
    }

    // ── Photo profil ──────────────────────────────────────────────
    @GetMapping("/citoyen/profil/photo")
    public String photoProfl(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("pageTitle", "Ma photo de profil");
        model.addAttribute("content", "citoyen/photo-profil");
        return "fragments/layout";
    }

    // ── Signaler ──────────────────────────────────────────────────
    @GetMapping("/citoyen/signaler")
    public String signaler(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("typesProblemes", typeProblemeService.getTousLesTypes());
        model.addAttribute("pageTitle", "Signaler un problème");
        model.addAttribute("content", "citoyen/signaler");
        return "fragments/layout";
    }

    // ── Mes signalements ──────────────────────────────────────────
    @GetMapping("/citoyen/mes-signalements")
    public String mesSignalements(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("problemes", citoyenService.voirProblemes(citoyen.getId()));
        model.addAttribute("pageTitle", "Mes signalements");
        model.addAttribute("content", "citoyen/mes-signalements");
        return "fragments/layout";
    }

    // ── Mes signalements EN COURS ─────────────────────────────────
    @GetMapping("/citoyen/mes-signalements/en-cours")
    public String mesSignalementsEnCours(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        List<Probleme> filtres = citoyenService.voirProblemes(citoyen.getId())
                .stream().filter(p -> p.getStatut().name().equals("EN_COURS")).toList();
        model.addAttribute("problemes", filtres);
        model.addAttribute("pageTitle", "Signalements en cours");
        model.addAttribute("content", "citoyen/mes-signalements");
        return "fragments/layout";
    }

    // ── Mes signalements RÉSOLUS ──────────────────────────────────
    @GetMapping("/citoyen/mes-signalements/resolus")
    public String mesSignalementsResolus(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        List<Probleme> filtres = citoyenService.voirProblemes(citoyen.getId())
                .stream().filter(p -> p.getStatut().name().equals("RESOLU")).toList();
        model.addAttribute("problemes", filtres);
        model.addAttribute("pageTitle", "Signalements résolus");
        model.addAttribute("content", "citoyen/mes-signalements");
        return "fragments/layout";
    }

    // ── Annonces ──────────────────────────────────────────────────
    @GetMapping("/citoyen/annonces")
    public String annonces(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("annonces", vitrineService.getToutesAnnonces());
        model.addAttribute("preferences",
                vitrineService.getPreferences(citoyen.getId()).orElse(null));
        model.addAttribute("categories", CategorieAnnonce.values());
        model.addAttribute("pageTitle", "Annonces des mairies");
        model.addAttribute("content", "citoyen/annonces");
        return "fragments/layout";
    }

    // ── Préférences notifications ──────────────────────────────────
    @GetMapping("/citoyen/preferences-notifications")
    public String preferences(Authentication auth, Model model) {
        Citoyen citoyen = getCitoyen(auth);
        common(citoyen, model);
        model.addAttribute("preferences",
                vitrineService.getPreferences(citoyen.getId()).orElse(null));
        model.addAttribute("categories", CategorieAnnonce.values());
        model.addAttribute("pageTitle", "Mes préférences de notifications");
        model.addAttribute("content", "citoyen/preferences-notifications");
        return "fragments/layout";
    }
}