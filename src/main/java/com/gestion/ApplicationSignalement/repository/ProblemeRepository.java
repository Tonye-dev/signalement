package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.enums.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProblemeRepository extends JpaRepository<Probleme, Long> {

    List<Probleme> findByCitoyens(Citoyen citoyen);
    List<Probleme> findByStatut(Statut statut);
    List<Probleme> findByTypeProbleme(TypeProbleme typeProbleme);
    List<Probleme> findByQuartier(String quartier);
    List<Probleme> findByMairie(Mairie mairie);
    List<Probleme> findByMairieAndStatut(Mairie mairie, Statut statut);
    List<Probleme> findByMairieAndDateSignalementBetween(Mairie mairie, LocalDate dateDebut, LocalDate dateFin);
    List<Probleme> findByMairieAndStatutAndDateSignalementBetween(Mairie mairie, Statut statut, LocalDate dateDebut, LocalDate dateFin);

    Long countByMairieNom(String nom);
    Long countByMairieNomAndStatut(String nom, Statut statut);
    long countByStatut(Statut statut);

    // ── Statistiques globales (SuperAdmin) ────────────────────────
    @Query("SELECT p.mairie.nom, COUNT(p) FROM Probleme p GROUP BY p.mairie.nom")
    List<Object[]> countGroupByMairie();

    @Query("SELECT p.mairie.nom, COUNT(p) FROM Probleme p WHERE p.statut = :statut GROUP BY p.mairie.nom")
    List<Object[]> countByMairieAndStatutGroupByMairie(@Param("statut") Statut statut);

    @Query("SELECT FUNCTION('DATE_FORMAT', p.dateSignalement, '%Y-%m'), COUNT(p) FROM Probleme p GROUP BY FUNCTION('DATE_FORMAT', p.dateSignalement, '%Y-%m') ORDER BY 1")
    List<Object[]> countByMonth();

    @Query("SELECT p.typeProbleme.nomType, COUNT(p) FROM Probleme p GROUP BY p.typeProbleme.nomType")
    List<Object[]> countGroupByType();

    // ── Stats avancées globales (SuperAdmin) ──────────────────────

    // Classement mairies par nombre de résolutions (toutes périodes)
    @Query("SELECT p.mairie.nom, COUNT(p) FROM Probleme p WHERE p.statut = 'RESOLU' GROUP BY p.mairie.nom ORDER BY COUNT(p) DESC")
    List<Object[]> classementMairiesParResolutions();

    // Classement mairies par temps moyen de résolution (plus rapide en premier)
    @Query(value = "SELECT m.nom, AVG(TIMESTAMPDIFF(HOUR, p.date_signalement, p.date_resolution)) FROM problemes p JOIN mairies m ON p.mairie_id = m.id WHERE p.statut = 'RESOLU' AND p.date_resolution IS NOT NULL GROUP BY m.nom ORDER BY 2 ASC", nativeQuery = true)
    List<Object[]> classementMairiesParRapidite();

    // Résolutions par mairie par mois
    @Query(value = "SELECT m.nom, DATE_FORMAT(p.date_resolution, '%Y-%m'), COUNT(*) FROM problemes p JOIN mairies m ON p.mairie_id = m.id WHERE p.statut = 'RESOLU' AND p.date_resolution IS NOT NULL GROUP BY m.nom, DATE_FORMAT(p.date_resolution, '%Y-%m') ORDER BY 2, 1", nativeQuery = true)
    List<Object[]> resolutionsParMairieParMois();

    // Types de problèmes les plus récurrents par quartier (global)
    @Query("SELECT p.quartier, p.typeProbleme.nomType, COUNT(p) FROM Probleme p WHERE p.typeProbleme IS NOT NULL GROUP BY p.quartier, p.typeProbleme.nomType ORDER BY p.quartier, COUNT(p) DESC")
    List<Object[]> typesParQuartierGlobal();

    // Temps moyen de résolution par type (global)
    @Query(value = "SELECT t.nom_type, AVG(TIMESTAMPDIFF(HOUR, p.date_signalement, p.date_resolution)) FROM problemes p JOIN typeproblemes t ON p.idtype = t.idtype WHERE p.statut = 'RESOLU' AND p.date_resolution IS NOT NULL GROUP BY t.nom_type ORDER BY 2 ASC", nativeQuery = true)
    List<Object[]> tempsMoyenResolutionParTypeGlobal();

    // ── Stats avancées par mairie ─────────────────────────────────

    // Top quartiers avec le plus de problèmes pour une mairie
    @Query("SELECT p.quartier, COUNT(p) FROM Probleme p WHERE p.mairie = :mairie GROUP BY p.quartier ORDER BY COUNT(p) DESC")
    List<Object[]> countByQuartierForMairie(@Param("mairie") Mairie mairie);

    // Types de problèmes les plus fréquents pour une mairie
    @Query("SELECT p.typeProbleme.nomType, COUNT(p) FROM Probleme p WHERE p.mairie = :mairie AND p.typeProbleme IS NOT NULL GROUP BY p.typeProbleme.nomType ORDER BY COUNT(p) DESC")
    List<Object[]> countByTypeForMairie(@Param("mairie") Mairie mairie);

    // Types de problèmes les plus fréquents PAR QUARTIER pour une mairie
    @Query("SELECT p.quartier, p.typeProbleme.nomType, COUNT(p) FROM Probleme p WHERE p.mairie = :mairie AND p.typeProbleme IS NOT NULL GROUP BY p.quartier, p.typeProbleme.nomType ORDER BY p.quartier, COUNT(p) DESC")
    List<Object[]> countByQuartierAndTypeForMairie(@Param("mairie") Mairie mairie);

    // Signalements vs résolus par mois pour une mairie
    @Query(value = "SELECT DATE_FORMAT(p.date_signalement, '%Y-%m'), p.statut, COUNT(*) FROM problemes p WHERE p.mairie_id = :mairieId GROUP BY DATE_FORMAT(p.date_signalement, '%Y-%m'), p.statut ORDER BY 1", nativeQuery = true)
    List<Object[]> countByMonthAndStatutForMairie(@Param("mairieId") Long mairieId);

    // Évolution par semaine pour une mairie
    @Query(value = "SELECT YEARWEEK(p.date_signalement, 1), COUNT(*) FROM problemes p WHERE p.mairie_id = :mairieId GROUP BY YEARWEEK(p.date_signalement, 1) ORDER BY 1", nativeQuery = true)
    List<Object[]> countByWeekForMairie(@Param("mairieId") Long mairieId);
}