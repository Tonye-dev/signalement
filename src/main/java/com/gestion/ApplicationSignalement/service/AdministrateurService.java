package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.PhotoResolution;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.Rapport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AdministrateurService {

    List<Probleme> voirProblemesAssignes(Long administrateurId);
    List<Probleme> filtrerProblemes(Long administrateurId, String statut,
                                    LocalDate dateDebut, LocalDate dateFin);
    Probleme changerStatutProbleme(Long problemeId, String nouveauStatut);

    
    List<PhotoResolution> ajouterPhotosResolution(Long problemeId,
                                                   Long administrateurId,
                                                   List<MultipartFile> photos) throws IOException;

    Rapport ajouterRapport(Long problemeId, Long administrateurId, String contenu);
    List<Rapport> voirRapportsMairie(Long administrateurId);
    List<String> voirPhotosProbleme(Long problemeId);
    boolean existeAdmin(String email);
}