package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import java.util.List;

public interface TypeProblemeService {

  
    // Récupérer un type de problème par son id
    TypeProbleme getTypeProblemeById(Long id);

    // Récupérer tous les types de problèmes
    List<TypeProbleme> getTousLesTypes();
}