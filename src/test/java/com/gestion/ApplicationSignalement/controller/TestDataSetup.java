package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.repository.AdministrateurRepository;
import com.gestion.ApplicationSignalement.repository.CitoyenRepository;
import com.gestion.ApplicationSignalement.repository.MairieRepository;
import com.gestion.ApplicationSignalement.repository.TypeProblemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDataSetup {

    @Autowired
    protected MairieRepository mairieRepository;

    @Autowired
    protected AdministrateurRepository administrateurRepository;

    @Autowired
    protected CitoyenRepository citoyenRepository;

    @Autowired
    protected TypeProblemeRepository typeProblemeRepository;

    protected Mairie mairieTest;
    protected Administrateur adminTest;
    protected Citoyen citoyenTest;
    protected TypeProbleme typeProblemeTest;

    @BeforeEach
    public void setup() {
        administrateurRepository.deleteAll();
        citoyenRepository.deleteAll();
        mairieRepository.deleteAll();
        typeProblemeRepository.deleteAll();

        // Mairie
        mairieTest = new Mairie();
        mairieTest.setNom("Mairie Test");
        mairieTest.setAdresse("1 rue de Test");
        mairieTest = mairieRepository.save(mairieTest);

        // Administrateur
        adminTest = new Administrateur();
        adminTest.setNom("Admin");
        adminTest.setPrenom("Test");
        adminTest.setEmail("admin@test.com");
        adminTest.setMotdepasse("1234");
        adminTest.setMairie(mairieTest);
        adminTest = administrateurRepository.save(adminTest);

        // Citoyen
        citoyenTest = new Citoyen();
        citoyenTest.setNom("Jean");
        citoyenTest.setPrenom("Dupont");
        citoyenTest.setEmail("jean@test.com");
        citoyenTest.setMotdepasse("1234");
        citoyenTest.setAdresse("1 rue du Test");
        citoyenTest.setTelephone("1234567890");
        citoyenTest = citoyenRepository.save(citoyenTest);

        // Type de problème
        typeProblemeTest = new TypeProbleme();
        typeProblemeTest.setNomType("Lampadaire");
        typeProblemeTest.setDescriptionType("Problème d'éclairage public");
        typeProblemeTest = typeProblemeRepository.save(typeProblemeTest);
    }
}