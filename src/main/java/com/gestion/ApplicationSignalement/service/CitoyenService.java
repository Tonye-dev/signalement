package com.gestion.ApplicationSignalement.service;

import java.util.List;

import com.gestion.ApplicationSignalement.entity.Probleme;

public interface CitoyenService {

   

     Probleme signalerProbleme(Long citoyenId, Probleme probleme, Long typeProblemeId);


    List<Probleme> voirProblemes(Long citoyenId);

    void supprimerProbleme(Long problemeId, Long citoyenId);

}