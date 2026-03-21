package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.repository.CitoyenRepository;
import com.gestion.ApplicationSignalement.repository.MairieRepository;
import com.gestion.ApplicationSignalement.repository.ProblemeRepository;
import com.gestion.ApplicationSignalement.repository.TypeProblemeRepository;
import com.gestion.enums.Statut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CitoyenServiceImpl implements CitoyenService {

    @Autowired
    private CitoyenRepository citoyenRepository;

    @Autowired
    private ProblemeRepository problemeRepository;

    @Autowired
    private MairieRepository mairieRepository;

    @Autowired
    private TypeProblemeRepository typeProblemeRepository;

    @Override
    public Probleme signalerProbleme(Long citoyenId, Probleme problemeSignale, Long typeProblemeId) {

        // 1. Récupérer le citoyen
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));

        // 2. Récupérer le type de problème
        TypeProbleme typeProbleme = typeProblemeRepository.findById(typeProblemeId)
                .orElseThrow(() -> new RuntimeException("Type de problème introuvable"));

        // 3. Attribution automatique de la mairie selon le quartier
        String quartier = problemeSignale.getQuartier();

        Optional<Mairie> mairieOpt = mairieRepository.findAll()
                .stream()
                .filter(m -> m.getZoneIntervention() != null
                        && m.getZoneIntervention().stream()
                            .anyMatch(z -> z.equalsIgnoreCase(quartier)))
                .findFirst();

        // Si aucune mairie ne supervise ce quartier, on prend la première mairie disponible
        // plutôt que de planter — l'admin pourra réassigner manuellement
        Mairie mairie = mairieOpt.orElseGet(() -> {
            List<Mairie> mairies = mairieRepository.findAll();
            if (mairies.isEmpty()) {
                throw new RuntimeException("Aucune mairie n'est enregistrée dans le système.");
            }
            return mairies.get(0);
        });

        // 4. Construire le problème — TOUS les champs copiés
        Probleme probleme = new Probleme();
        probleme.setTitre(problemeSignale.getTitre());           // ← était manquant !
        probleme.setDescription(problemeSignale.getDescription());
        probleme.setQuartier(quartier);
        probleme.setMairie(mairie);
        probleme.setStatut(Statut.SIGNALE);
        probleme.setDateSignalement(LocalDateTime.now());
        probleme.setTypeProbleme(typeProbleme);

        // 5. Ajouter le citoyen
        List<Citoyen> listeCitoyens = new ArrayList<>();
        listeCitoyens.add(citoyen);
        probleme.setCitoyens(listeCitoyens);

        // 6. Sauvegarder
        return problemeRepository.save(probleme);
    }

    @Override
    public List<Probleme> voirProblemes(Long citoyenId) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));
        return problemeRepository.findByCitoyens(citoyen);
    }

    @Override
    public void supprimerProbleme(Long citoyenId, Long problemeId) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));

        Probleme probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new RuntimeException("Problème introuvable"));

        if (!probleme.getCitoyens().contains(citoyen)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer ce problème");
        }

        probleme.getCitoyens().remove(citoyen);

        if (probleme.getCitoyens().isEmpty()) {
            problemeRepository.delete(probleme);
        } else {
            problemeRepository.save(probleme);
        }
    }
}