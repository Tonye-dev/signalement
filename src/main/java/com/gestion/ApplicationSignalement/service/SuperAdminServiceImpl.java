package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.SuperAdmin;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.repository.AdministrateurRepository;
import com.gestion.ApplicationSignalement.repository.MairieRepository;
import com.gestion.ApplicationSignalement.repository.SuperAdminRepository;
import com.gestion.ApplicationSignalement.repository.TypeProblemeRepository;
import com.gestion.enums.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private MairieRepository mairieRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private TypeProblemeRepository typeProblemeRepository;

    @Autowired
      private  PasswordEncoder passwordEncoder;

    // ── Mairies ───────────────────────────────────────────────────

    @Override
    public Mairie ajouterMairie(Mairie mairie) {
        return mairieRepository.save(mairie);
    }

    @Override
    public Mairie modifierMairie(Long mairieId, Mairie mairieDetails) {
        Mairie mairie = mairieRepository.findById(mairieId)
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));
        mairie.setNom(mairieDetails.getNom());
        mairie.setAdresse(mairieDetails.getAdresse());
        mairie.setZoneIntervention(mairieDetails.getZoneIntervention());
        return mairieRepository.save(mairie);
    }

    @Override
    public List<Mairie> listerMairies() {
        return mairieRepository.findAll();
    }

    @Override
    public Mairie getMairieById(Long id) {
        return mairieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mairie introuvable avec l'id : " + id));
    }

    // ── Administrateurs ───────────────────────────────────────────

    @Override
    public Administrateur ajouterAdministrateur(Administrateur administrateur, Long mairieId) {
        Mairie mairie = mairieRepository.findById(mairieId)
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));
        administrateur.setMairie(mairie);
         String motDePasseEncode = passwordEncoder.encode(administrateur.getMotdepasse());
    administrateur.setMotdepasse(motDePasseEncode);

        return administrateurRepository.save(administrateur);
    }

    @Override
    public List<Administrateur> listerAdministrateurs() {
        return administrateurRepository.findAll();
    }

    // ── Types de problèmes ────────────────────────────────────────

    @Override
    public TypeProbleme creerTypeProbleme(String nomType) {
        TypeProbleme typeProbleme = new TypeProbleme(nomType);
        return typeProblemeRepository.save(typeProbleme);
    }

    @Override
    public List<TypeProbleme> listerTypesProblemes() {
        return typeProblemeRepository.findAll();
    }

    // ── Super Admin ───────────────────────────────────────────────

    @Override
    public SuperAdmin ajouterSuperAdmin(SuperAdmin superAdmin) {
        superAdmin.setRole(Role.SUPERADMIN);
        superAdmin.setValide(true);
        return superAdminRepository.save(superAdmin);
    }

    @Override
    public boolean existeSuperAdmin(String email) {
        return superAdminRepository.findByEmail(email).isPresent();
    }
}