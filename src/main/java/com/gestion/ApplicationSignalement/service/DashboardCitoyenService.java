package com.gestion.ApplicationSignalement.service;

import com.gestion.dto.DashboardCitoyenDto;
import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.enums.Statut;
import com.gestion.ApplicationSignalement.repository.CitoyenRepository;
import com.gestion.ApplicationSignalement.repository.ProblemeRepository;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardCitoyenService {

    private final CitoyenRepository citoyenRepository;
    private final ProblemeRepository problemeRepository;

    public DashboardCitoyenService(CitoyenRepository citoyenRepository,
                                   ProblemeRepository problemeRepository) {
        this.citoyenRepository  = citoyenRepository;
        this.problemeRepository = problemeRepository;
    }

    public DashboardCitoyenDto getDashboard(Long idCitoyen) {

        Citoyen citoyen = citoyenRepository.findById(idCitoyen)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));

        List<Probleme> problemes = problemeRepository.findByCitoyens(citoyen);

        long totalProblemes      = problemes.size();
        long problemesSignales   = 0;
        long problemesEnCours    = 0;
        long problemesResolus    = 0;
        long totalPhotosAjoutees = 0;

        Map<String, Long> repartitionParType = new LinkedHashMap<>();
        Map<String, Long> problemesParMois   = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Probleme p : problemes) {

            // ── Statut ──
            if (p.getStatut() == Statut.SIGNALE)   problemesSignales++;
            else if (p.getStatut() == Statut.EN_COURS) problemesEnCours++;
            else if (p.getStatut() == Statut.RESOLU)   problemesResolus++;

            // ── Photos ── protection null
            if (p.getPhotos() != null) {
                totalPhotosAjoutees += p.getPhotos().size();
            }

            // ── Type ── protection null
            String type = (p.getTypeProbleme() != null)
                    ? p.getTypeProbleme().getNomType()
                    : "Non défini";
            repartitionParType.put(type, repartitionParType.getOrDefault(type, 0L) + 1);

            // ── Mois ── protection null
            if (p.getDateSignalement() != null) {
                String mois = p.getDateSignalement().format(formatter);
                problemesParMois.put(mois, problemesParMois.getOrDefault(mois, 0L) + 1);
            }
        }

        return new DashboardCitoyenDto(
                totalProblemes,
                problemesSignales,
                problemesEnCours,
                problemesResolus,
                totalPhotosAjoutees,
                repartitionParType,
                problemesParMois
        );
    }
}