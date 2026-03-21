package com.gestion.ApplicationSignalement.service;

import com.gestion.dto.DashboardAdministrateurDto;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.enums.Statut;
import com.gestion.ApplicationSignalement.repository.MairieRepository;
import com.gestion.ApplicationSignalement.repository.ProblemeRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardAdministrateurService {

    private final ProblemeRepository problemeRepository;
    private final MairieRepository   mairieRepository;

    public DashboardAdministrateurService(ProblemeRepository problemeRepository,
                                          MairieRepository mairieRepository) {
        this.problemeRepository = problemeRepository;
        this.mairieRepository   = mairieRepository;
    }

    // ── Helper List<Object[]> → Map<String, Long> ─────────────────
    private Map<String, Long> toMapLong(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        if (rows != null) rows.forEach(r ->
                map.put(r[0] != null ? r[0].toString() : "—",
                        r[1] != null ? ((Number) r[1]).longValue() : 0L));
        return map;
    }

    public DashboardAdministrateurDto getDashboard(Long idMairie) {

        Mairie mairie = mairieRepository.findById(idMairie)
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));

        List<Probleme> tous = problemeRepository.findByMairie(mairie);

        // ── Totaux ────────────────────────────────────────────────
        long totalProblemes     = tous.size();
        long problemesEnAttente = tous.stream().filter(p -> p.getStatut() == Statut.EN_COURS).count();
        long problemesResolus   = tous.stream().filter(p -> p.getStatut() == Statut.RESOLU).count();

        // ── Répartition par type ──────────────────────────────────
        Map<String, Long> repartitionParType = new LinkedHashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> problemesParMois   = new LinkedHashMap<>();

        for (Probleme p : tous) {
            String type = p.getTypeProbleme() != null ? p.getTypeProbleme().getNomType() : "Non défini";
            repartitionParType.merge(type, 1L, Long::sum);
            if (p.getDateSignalement() != null)
                problemesParMois.merge(p.getDateSignalement().format(fmt), 1L, Long::sum);
        }

        // ── Temps de résolution ───────────────────────────────────
        List<Probleme> resolus = tous.stream()
                .filter(p -> p.getStatut() == Statut.RESOLU
                          && p.getDateSignalement() != null
                          && p.getDateResolution() != null)
                .collect(Collectors.toList());

        double tempsMoyenGlobal = resolus.isEmpty() ? 0 :
                resolus.stream()
                        .mapToLong(p -> ChronoUnit.HOURS.between(p.getDateSignalement(), p.getDateResolution()))
                        .average().orElse(0);

        Map<String, Double> tempsMoyenParType = resolus.stream()
                .filter(p -> p.getTypeProbleme() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getTypeProbleme().getNomType(),
                        Collectors.averagingLong(p -> ChronoUnit.HOURS.between(
                                p.getDateSignalement(), p.getDateResolution()))
                ));

        // ── Stats avancées ────────────────────────────────────────

        // Top quartiers
        Map<String, Long> topQuartiers =
                toMapLong(problemeRepository.countByQuartierForMairie(mairie));

        // Types fréquents
        Map<String, Long> typesFrequents =
                toMapLong(problemeRepository.countByTypeForMairie(mairie));

        // Signalements vs résolus par mois
        List<Object[]> rawMoisStatut =
                problemeRepository.countByMonthAndStatutForMairie(mairie.getId());
        Map<String, Long> signalesParMois  = new LinkedHashMap<>();
        Map<String, Long> resolusParMois   = new LinkedHashMap<>();
        if (rawMoisStatut != null) {
            rawMoisStatut.forEach(r -> {
                String mois   = r[0] != null ? r[0].toString() : "—";
                String statut = r[1] != null ? r[1].toString() : "";
                long count    = r[2] != null ? ((Number) r[2]).longValue() : 0L;
                if ("SIGNALE".equals(statut) || "EN_COURS".equals(statut))
                    signalesParMois.merge(mois, count, Long::sum);
                else if ("RESOLU".equals(statut))
                    resolusParMois.merge(mois, count, Long::sum);
            });
        }

        // Évolution par semaine
        Map<String, Long> evolutionParSemaine =
                toMapLong(problemeRepository.countByWeekForMairie(mairie.getId()));

        // Types par quartier
        List<Object[]> rawQT = problemeRepository.countByQuartierAndTypeForMairie(mairie);
        Map<String, Map<String, Long>> typesParQuartier = new LinkedHashMap<>();
        if (rawQT != null) {
            rawQT.forEach(r -> {
                String quartier = r[0] != null ? r[0].toString() : "—";
                String type     = r[1] != null ? r[1].toString() : "—";
                long   count    = r[2] != null ? ((Number) r[2]).longValue() : 0L;
                typesParQuartier.computeIfAbsent(quartier, k -> new LinkedHashMap<>())
                                .put(type, count);
            });
        }

        return new DashboardAdministrateurDto(
                totalProblemes, problemesEnAttente, problemesResolus,
                repartitionParType, problemesParMois,
                tempsMoyenGlobal, tempsMoyenParType,
                topQuartiers, typesFrequents,
                signalesParMois, resolusParMois,
                evolutionParSemaine, typesParQuartier
        );
    }
}