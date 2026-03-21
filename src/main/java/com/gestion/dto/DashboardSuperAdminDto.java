package com.gestion.dto;

import java.util.Map;

public class DashboardSuperAdminDto {

    // ── Totaux globaux ────────────────────────────────────────────
    private long totalMairies;
    private long totalAdministrateurs;
    private long totalTypesProblemes;
    private long totalUtilisateurs;
    private long totalProblemes;
    private long problemesEnAttente;
    private long problemesResolus;

    // ── Stats de base ─────────────────────────────────────────────
    private Map<String, Long> problemesParMairie;
    private Map<String, Long> enCoursParMairie;
    private Map<String, Long> resolusParMairie;
    private Map<String, Long> problemesParMois;
    private Map<String, Long> repartitionParTypeProbleme;

    // ── Classements mairies ───────────────────────────────────────
    private Map<String, Long>   classementResolutions;   // mairie → nb résolutions (desc)
    private Map<String, Double> classementRapidite;      // mairie → temps moyen heures (asc)
    private Map<String, Map<String, Long>> resolutionsParMairieParMois; // mois → {mairie → nb}

    // ── Stats types et quartiers (global) ────────────────────────
    private Map<String, Double> tempsMoyenParTypeGlobal;          // type → heures moy
    private Map<String, Map<String, Long>> typesParQuartierGlobal; // quartier → {type → nb}

    public DashboardSuperAdminDto() {}

    // Constructeur complet
    public DashboardSuperAdminDto(
            long totalMairies, long totalAdministrateurs,
            long totalTypesProblemes, long totalUtilisateurs,
            long totalProblemes, long problemesEnAttente, long problemesResolus,
            Map<String, Long> problemesParMairie,
            Map<String, Long> enCoursParMairie,
            Map<String, Long> resolusParMairie,
            Map<String, Long> problemesParMois,
            Map<String, Long> repartitionParTypeProbleme,
            Map<String, Long> classementResolutions,
            Map<String, Double> classementRapidite,
            Map<String, Map<String, Long>> resolutionsParMairieParMois,
            Map<String, Double> tempsMoyenParTypeGlobal,
            Map<String, Map<String, Long>> typesParQuartierGlobal) {

        this.totalMairies               = totalMairies;
        this.totalAdministrateurs       = totalAdministrateurs;
        this.totalTypesProblemes        = totalTypesProblemes;
        this.totalUtilisateurs          = totalUtilisateurs;
        this.totalProblemes             = totalProblemes;
        this.problemesEnAttente         = problemesEnAttente;
        this.problemesResolus           = problemesResolus;
        this.problemesParMairie         = problemesParMairie;
        this.enCoursParMairie           = enCoursParMairie;
        this.resolusParMairie           = resolusParMairie;
        this.problemesParMois           = problemesParMois;
        this.repartitionParTypeProbleme = repartitionParTypeProbleme;
        this.classementResolutions      = classementResolutions;
        this.classementRapidite         = classementRapidite;
        this.resolutionsParMairieParMois = resolutionsParMairieParMois;
        this.tempsMoyenParTypeGlobal    = tempsMoyenParTypeGlobal;
        this.typesParQuartierGlobal     = typesParQuartierGlobal;
    }

    // ── Helper formatage temps ─────────────────────────────────────
    public static String formaterHeures(double heures) {
        if (heures <= 0) return "—";
        long h = (long) heures;
        if (h < 24) return h + "h";
        return (h / 24) + "j " + (h % 24) + "h";
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public long getTotalMairies() { return totalMairies; }
    public void setTotalMairies(long v) { this.totalMairies = v; }
    public long getTotalAdministrateurs() { return totalAdministrateurs; }
    public void setTotalAdministrateurs(long v) { this.totalAdministrateurs = v; }
    public long getTotalTypesProblemes() { return totalTypesProblemes; }
    public void setTotalTypesProblemes(long v) { this.totalTypesProblemes = v; }
    public long getTotalUtilisateurs() { return totalUtilisateurs; }
    public void setTotalUtilisateurs(long v) { this.totalUtilisateurs = v; }
    public long getTotalProblemes() { return totalProblemes; }
    public void setTotalProblemes(long v) { this.totalProblemes = v; }
    public long getProblemesEnAttente() { return problemesEnAttente; }
    public void setProblemesEnAttente(long v) { this.problemesEnAttente = v; }
    public long getProblemesResolus() { return problemesResolus; }
    public void setProblemesResolus(long v) { this.problemesResolus = v; }
    public Map<String, Long> getProblemesParMairie() { return problemesParMairie; }
    public void setProblemesParMairie(Map<String, Long> m) { this.problemesParMairie = m; }
    public Map<String, Long> getEnCoursParMairie() { return enCoursParMairie; }
    public void setEnCoursParMairie(Map<String, Long> m) { this.enCoursParMairie = m; }
    public Map<String, Long> getResolusParMairie() { return resolusParMairie; }
    public void setResolusParMairie(Map<String, Long> m) { this.resolusParMairie = m; }
    public Map<String, Long> getProblemesParMois() { return problemesParMois; }
    public void setProblemesParMois(Map<String, Long> m) { this.problemesParMois = m; }
    public Map<String, Long> getRepartitionParTypeProbleme() { return repartitionParTypeProbleme; }
    public void setRepartitionParTypeProbleme(Map<String, Long> m) { this.repartitionParTypeProbleme = m; }
    public Map<String, Long> getClassementResolutions() { return classementResolutions; }
    public void setClassementResolutions(Map<String, Long> m) { this.classementResolutions = m; }
    public Map<String, Double> getClassementRapidite() { return classementRapidite; }
    public void setClassementRapidite(Map<String, Double> m) { this.classementRapidite = m; }
    public Map<String, Map<String, Long>> getResolutionsParMairieParMois() { return resolutionsParMairieParMois; }
    public void setResolutionsParMairieParMois(Map<String, Map<String, Long>> m) { this.resolutionsParMairieParMois = m; }
    public Map<String, Double> getTempsMoyenParTypeGlobal() { return tempsMoyenParTypeGlobal; }
    public void setTempsMoyenParTypeGlobal(Map<String, Double> m) { this.tempsMoyenParTypeGlobal = m; }
    public Map<String, Map<String, Long>> getTypesParQuartierGlobal() { return typesParQuartierGlobal; }
    public void setTypesParQuartierGlobal(Map<String, Map<String, Long>> m) { this.typesParQuartierGlobal = m; }
}