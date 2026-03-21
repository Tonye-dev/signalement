package com.gestion.ApplicationSignalement.service;

import com.gestion.dto.DashboardSuperAdminDto;
import com.gestion.enums.Statut;
import com.gestion.ApplicationSignalement.repository.*;
import com.gestion.enums.Role;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardSuperAdminService {

    private final MairieRepository       mairieRepository;
    private final UtilisateurRepository  utilisateurRepository;
    private final TypeProblemeRepository typeProblemeRepository;
    private final ProblemeRepository     problemeRepository;

    public DashboardSuperAdminService(MairieRepository mairieRepository,
                                      UtilisateurRepository utilisateurRepository,
                                      TypeProblemeRepository typeProblemeRepository,
                                      ProblemeRepository problemeRepository) {
        this.mairieRepository       = mairieRepository;
        this.utilisateurRepository  = utilisateurRepository;
        this.typeProblemeRepository = typeProblemeRepository;
        this.problemeRepository     = problemeRepository;
    }

    // ── Helper List<Object[]> → Map<String, Long> ─────────────────
    private Map<String, Long> toMapLong(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        if (rows != null) rows.forEach(r ->
                map.put(r[0] != null ? r[0].toString() : "—",
                        r[1] != null ? ((Number) r[1]).longValue() : 0L));
        return map;
    }

    // ── Helper List<Object[]> → Map<String, Double> ───────────────
    private Map<String, Double> toMapDouble(List<Object[]> rows) {
        Map<String, Double> map = new LinkedHashMap<>();
        if (rows != null) rows.forEach(r ->
                map.put(r[0] != null ? r[0].toString() : "—",
                        r[1] != null ? ((Number) r[1]).doubleValue() : 0.0));
        return map;
    }

    public DashboardSuperAdminDto getDashboard() {

        // ── Totaux globaux ────────────────────────────────────────
        long totalMairies         = mairieRepository.count();
        long totalAdministrateurs = utilisateurRepository.countByRole(Role.ADMINISTRATEUR);
        long totalTypesProblemes  = typeProblemeRepository.count();
        long totalUtilisateurs    = utilisateurRepository.count();
        long totalProblemes       = problemeRepository.count();
        long problemesEnAttente   = problemeRepository.countByStatut(Statut.EN_COURS);
        long problemesResolus     = problemeRepository.countByStatut(Statut.RESOLU);

        // ── Stats de base ─────────────────────────────────────────
        Map<String, Long> problemesParMairie = toMapLong(problemeRepository.countGroupByMairie());
        Map<String, Long> enCoursParMairie   = toMapLong(problemeRepository.countByMairieAndStatutGroupByMairie(Statut.EN_COURS));
        Map<String, Long> resolusParMairie   = toMapLong(problemeRepository.countByMairieAndStatutGroupByMairie(Statut.RESOLU));
        Map<String, Long> problemesParMois   = toMapLong(problemeRepository.countByMonth());
        Map<String, Long> repartitionParType = toMapLong(problemeRepository.countGroupByType());

        // ── Classement mairies par résolutions ────────────────────
        Map<String, Long> classementResolutions =
                toMapLong(problemeRepository.classementMairiesParResolutions());

        // ── Classement mairies par rapidité ───────────────────────
        Map<String, Double> classementRapidite =
                toMapDouble(problemeRepository.classementMairiesParRapidite());

        // ── Résolutions par mairie par mois ───────────────────────
        // Structure : mois → { mairie → nb }
        List<Object[]> rawMairiesMois = problemeRepository.resolutionsParMairieParMois();
        Map<String, Map<String, Long>> resolutionsParMairieParMois = new LinkedHashMap<>();
        if (rawMairiesMois != null) {
            rawMairiesMois.forEach(r -> {
                String mairie = r[0] != null ? r[0].toString() : "—";
                String mois   = r[1] != null ? r[1].toString() : "—";
                long   count  = r[2] != null ? ((Number) r[2]).longValue() : 0L;
                // On organise par mois → {mairie → count}
                resolutionsParMairieParMois
                        .computeIfAbsent(mois, k -> new LinkedHashMap<>())
                        .put(mairie, count);
            });
        }

        // ── Temps moyen résolution par type (global) ──────────────
        Map<String, Double> tempsMoyenParTypeGlobal =
                toMapDouble(problemeRepository.tempsMoyenResolutionParTypeGlobal());

        // ── Types par quartier (global) ───────────────────────────
        List<Object[]> rawQT = problemeRepository.typesParQuartierGlobal();
        Map<String, Map<String, Long>> typesParQuartierGlobal = new LinkedHashMap<>();
        if (rawQT != null) {
            rawQT.forEach(r -> {
                String quartier = r[0] != null ? r[0].toString() : "—";
                String type     = r[1] != null ? r[1].toString() : "—";
                long   count    = r[2] != null ? ((Number) r[2]).longValue() : 0L;
                typesParQuartierGlobal
                        .computeIfAbsent(quartier, k -> new LinkedHashMap<>())
                        .merge(type, count, Long::sum);
            });
        }

        return new DashboardSuperAdminDto(
                totalMairies, totalAdministrateurs, totalTypesProblemes,
                totalUtilisateurs, totalProblemes, problemesEnAttente, problemesResolus,
                problemesParMairie, enCoursParMairie, resolusParMairie,
                problemesParMois, repartitionParType,
                classementResolutions, classementRapidite, resolutionsParMairieParMois,
                tempsMoyenParTypeGlobal, typesParQuartierGlobal
        );
    }
}