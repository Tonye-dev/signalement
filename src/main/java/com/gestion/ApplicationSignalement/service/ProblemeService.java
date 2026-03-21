package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.repository.ProblemeRepository;
import com.gestion.enums.Statut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemeService {

    @Autowired
    private ProblemeRepository problemeRepository;

    // Sauvegarder un problème
    public Probleme save(Probleme probleme) {
        return problemeRepository.save(probleme);
    }

    // Récupérer tous les problèmes
    public List<Probleme> getTousLesProblemes() {
        return problemeRepository.findAll();
    }

    // Récupérer un problème par ID
    public Probleme findByIdProbleme(Long id) {
        return problemeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problème non trouvé"));
    }

    // Lister les problèmes par type
    public List<Probleme> listerProblemesParType(TypeProbleme typeProbleme) {
        return problemeRepository.findByTypeProbleme(typeProbleme);
    }

    // Filtrer les problèmes par statut
    public List<Probleme> filtrerProblemesParStatut(Statut statut) {
        // Ici tu peux convertir le String en Enum si ton champ statut est un Enum
        return problemeRepository.findByStatut(statut);
    }

   
}