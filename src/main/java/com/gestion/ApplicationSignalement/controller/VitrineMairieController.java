package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.entity.*;
import com.gestion.ApplicationSignalement.repository.ProblemeRepository;
import com.gestion.ApplicationSignalement.service.VitrineMairieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VitrineMairieController {

    @Autowired private VitrineMairieService vitrineService;
    @Autowired private ProblemeRepository   problemeRepository;

    @GetMapping("/mairies")
    public String listeMairies(Model model) {
        model.addAttribute("mairies", vitrineService.listerToutesMairies());
        model.addAttribute("pageTitle", "Les mairies");
        model.addAttribute("content", "vitrine/liste-mairies");
        return "fragments/layout";
    }

    @GetMapping("/mairies/{id}")
    public String vitrine(@PathVariable Long id, Model model) {
        Mairie mairie = vitrineService.getMairie(id);
        model.addAttribute("mairie", mairie);
        model.addAttribute("pageTitle", mairie.getNom());
        model.addAttribute("content", "vitrine/mairie");
        return "fragments/layout";
    }

    @GetMapping("/mairies/{id}/annonces")
    public String annonces(@PathVariable Long id, Model model) {
        Mairie mairie = vitrineService.getMairie(id);
        model.addAttribute("mairie", mairie);
        model.addAttribute("annonces", vitrineService.getAnnonces(id));
        model.addAttribute("categories", CategorieAnnonce.values());
        model.addAttribute("pageTitle", "Annonces — " + mairie.getNom());
        model.addAttribute("content", "vitrine/annonces");
        return "fragments/layout";
    }

    @GetMapping("/mairies/{id}/services")
    public String services(@PathVariable Long id, Model model) {
        Mairie mairie = vitrineService.getMairie(id);
        model.addAttribute("mairie", mairie);
        model.addAttribute("services", vitrineService.getServices(id));
        model.addAttribute("pageTitle", "Services — " + mairie.getNom());
        model.addAttribute("content", "vitrine/services");
        return "fragments/layout";
    }

    @GetMapping("/mairies/{id}/personnel")
    public String personnel(@PathVariable Long id, Model model) {
        Mairie mairie = vitrineService.getMairie(id);
        model.addAttribute("mairie", mairie);
        model.addAttribute("agents", vitrineService.getAgents(id));
        model.addAttribute("services", vitrineService.getServices(id));
        model.addAttribute("pageTitle", "Personnel — " + mairie.getNom());
        model.addAttribute("content", "vitrine/personnel");
        return "fragments/layout";
    }

    @GetMapping("/citoyen/problemes-quartier")
    public String problemesParQuartier(Model model) {
        List<Probleme> tous = problemeRepository.findAll();
        List<String> quartiers = tous.stream()
                .map(Probleme::getQuartier)
                .filter(q -> q != null && !q.isBlank())
                .distinct().sorted().toList();
        model.addAttribute("problemes", tous);
        model.addAttribute("quartiers", quartiers);
        model.addAttribute("pageTitle", "Problèmes par quartier");
        model.addAttribute("content", "citoyen/problemes-quartier");
        return "fragments/layout";
    }
}