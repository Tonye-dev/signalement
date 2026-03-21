package com.gestion.dto;

import java.util.Map;

/**
 * DTO pour le dashboard du CITOYEN
 * Contient les statistiques personnelles du citoyen connecté
 */
public class DashboardCitoyenDto {

    // 🔹 Totaux globaux
    private long totalProblemes;          // Nombre total de problèmes signalés
    private long problemesSignales;       // Statut SIGNALE
    private long problemesEnCours;        // Statut EN_COURS
    private long problemesResolus;        // Statut RESOLU
    private long totalPhotosAjoutees;     // Nombre total de photos ajoutées

    // 🔹 Répartition
    private Map<String, Long> repartitionParType;   // Type de problème → nombre
    private Map<String, Long> problemesParMois;     // yyyy-MM → nombre

    // 🔹 Constructeur vide (important pour Jackson)
    public DashboardCitoyenDto() {
    }

    // 🔹 Constructeur complet
    public DashboardCitoyenDto(long totalProblemes,
                               long problemesSignales,
                               long problemesEnCours,
                               long problemesResolus,
                               long totalPhotosAjoutees,
                               Map<String, Long> repartitionParType,
                               Map<String, Long> problemesParMois) {
        this.totalProblemes = totalProblemes;
        this.problemesSignales = problemesSignales;
        this.problemesEnCours = problemesEnCours;
        this.problemesResolus = problemesResolus;
        this.totalPhotosAjoutees = totalPhotosAjoutees;
        this.repartitionParType = repartitionParType;
        this.problemesParMois = problemesParMois;
    }

    // 🔹 Getters & Setters

    public long getTotalProblemes() {
        return totalProblemes;
    }

    public void setTotalProblemes(long totalProblemes) {
        this.totalProblemes = totalProblemes;
    }

    public long getProblemesSignales() {
        return problemesSignales;
    }

    public void setProblemesSignales(long problemesSignales) {
        this.problemesSignales = problemesSignales;
    }

    public long getProblemesEnCours() {
        return problemesEnCours;
    }

    public void setProblemesEnCours(long problemesEnCours) {
        this.problemesEnCours = problemesEnCours;
    }

    public long getProblemesResolus() {
        return problemesResolus;
    }

    public void setProblemesResolus(long problemesResolus) {
        this.problemesResolus = problemesResolus;
    }

    public long getTotalPhotosAjoutees() {
        return totalPhotosAjoutees;
    }

    public void setTotalPhotosAjoutees(long totalPhotosAjoutees) {
        this.totalPhotosAjoutees = totalPhotosAjoutees;
    }

    public Map<String, Long> getRepartitionParType() {
        return repartitionParType;
    }

    public void setRepartitionParType(Map<String, Long> repartitionParType) {
        this.repartitionParType = repartitionParType;
    }

    public Map<String, Long> getProblemesParMois() {
        return problemesParMois;
    }

    public void setProblemesParMois(Map<String, Long> problemesParMois) {
        this.problemesParMois = problemesParMois;
    }
}