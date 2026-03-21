package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.repository.TypeProblemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeProblemeServiceImpl implements TypeProblemeService {

    @Autowired
    private TypeProblemeRepository typeProblemeRepository;

  

    @Override
    public TypeProbleme getTypeProblemeById(Long id) {
        return typeProblemeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de problème non trouvé"));
    }

    @Override
    public List<TypeProbleme> getTousLesTypes() {
        return typeProblemeRepository.findAll();
    }
}