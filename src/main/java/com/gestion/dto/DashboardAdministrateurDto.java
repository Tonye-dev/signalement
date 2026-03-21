package com.gestion.dto;

import java.util.Map;

public class DashboardAdministrateurDto {

    // ── Totaux ────────────────────────────────────────────────────
    private long totalProblemes;
    private long problemesEnAttente;
    private long problemesResolus;

    // ── Stats de base ─────────────────────────────────────────────
    private Map<String, Long> repartitionParTypeProbleme;
    private Map<String, Long> problemesParMois;

    // ── Temps de résolution ───────────────────────────────────────
    private double tempsMoyenResolutionHeures;
    private Map<String, Double> tempsMoyenParType;

    // ── Stats avancées ────────────────────────────────────────────
    private Map<String, Long> topQuartiers;          // quartiers avec le plus de problèmes
    private Map<String, Long> typesFrequents;         // types les plus fréquents
    private Map<String, Long> signalesParMois;        // signalements par mois
    private Map<String, Long> resolusParMois;         // résolus par mois
    private Map<String, Long> evolutionParSemaine;    // évolution hebdomadaire
    private Map<String, Map<String, Long>> typesParQuartier; // types par quartier

    public DashboardAdministrateurDto() {}

    // Constructeur complet
    public DashboardAdministrateurDto(
            long totalProblemes, long problemesEnAttente, long problemesResolus,
            Map<String, Long> repartitionParTypeProbleme, Map<String, Long> problemesParMois,
            double tempsMoyenResolutionHeures, Map<String, Double> tempsMoyenParType,
            Map<String, Long> topQuartiers, Map<String, Long> typesFrequents,
            Map<String, Long> signalesParMois, Map<String, Long> resolusParMois,
            Map<String, Long> evolutionParSemaine,
            Map<String, Map<String, Long>> typesParQuartier) {

        this.totalProblemes             = totalProblemes;
        this.problemesEnAttente         = problemesEnAttente;
        this.problemesResolus           = problemesResolus;
        this.repartitionParTypeProbleme = repartitionParTypeProbleme;
        this.problemesParMois           = problemesParMois;
        this.tempsMoyenResolutionHeures = tempsMoyenResolutionHeures;
        this.tempsMoyenParType          = tempsMoyenParType;
        this.topQuartiers               = topQuartiers;
        this.typesFrequents             = typesFrequents;
        this.signalesParMois            = signalesParMois;
        this.resolusParMois             = resolusParMois;
        this.evolutionParSemaine        = evolutionParSemaine;
        this.typesParQuartier           = typesParQuartier;
    }

    // ── Helper formatage temps ─────────────────────────────────────
    public String getTempsMoyenResolutionFormate() {
        if (tempsMoyenResolutionHeures <= 0) return "—";
        long h = (long) tempsMoyenResolutionHeures;
        if (h < 24) return h + "h";
        return (h / 24) + "j " + (h % 24) + "h";
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public long getTotalProblemes() { return totalProblemes; }
    public void setTotalProblemes(long t) { this.totalProblemes = t; }
    public long getProblemesEnAttente() { return problemesEnAttente; }
    public void setProblemesEnAttente(long p) { this.problemesEnAttente = p; }
    public long getProblemesResolus() { return problemesResolus; }
    public void setProblemesResolus(long p) { this.problemesResolus = p; }
    public Map<String, Long> getRepartitionParTypeProbleme() { return repartitionParTypeProbleme; }
    public void setRepartitionParTypeProbleme(Map<String, Long> m) { this.repartitionParTypeProbleme = m; }
    public Map<String, Long> getProblemesParMois() { return problemesParMois; }
    public void setProblemesParMois(Map<String, Long> m) { this.problemesParMois = m; }
    public double getTempsMoyenResolutionHeures() { return tempsMoyenResolutionHeures; }
    public void setTempsMoyenResolutionHeures(double t) { this.tempsMoyenResolutionHeures = t; }
    public Map<String, Double> getTempsMoyenParType() { return tempsMoyenParType; }
    public void setTempsMoyenParType(Map<String, Double> m) { this.tempsMoyenParType = m; }
    public Map<String, Long> getTopQuartiers() { return topQuartiers; }
    public void setTopQuartiers(Map<String, Long> m) { this.topQuartiers = m; }
    public Map<String, Long> getTypesFrequents() { return typesFrequents; }
    public void setTypesFrequents(Map<String, Long> m) { this.typesFrequents = m; }
    public Map<String, Long> getSignalesParMois() { return signalesParMois; }
    public void setSignalesParMois(Map<String, Long> m) { this.signalesParMois = m; }
    public Map<String, Long> getResolusParMois() { return resolusParMois; }
    public void setResolusParMois(Map<String, Long> m) { this.resolusParMois = m; }
    public Map<String, Long> getEvolutionParSemaine() { return evolutionParSemaine; }
    public void setEvolutionParSemaine(Map<String, Long> m) { this.evolutionParSemaine = m; }
    public Map<String, Map<String, Long>> getTypesParQuartier() { return typesParQuartier; }
    public void setTypesParQuartier(Map<String, Map<String, Long>> m) { this.typesParQuartier = m; }
}