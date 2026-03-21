package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.SuperAdmin;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;

import java.util.List;

public interface SuperAdminService {

    // ── Mairies ───────────────────────────────────────────────────
    Mairie ajouterMairie(Mairie mairie);
    Mairie modifierMairie(Long mairieId, Mairie mairieDetails);
    List<Mairie> listerMairies();
    Mairie getMairieById(Long id);

    // ── Administrateurs ───────────────────────────────────────────
    Administrateur ajouterAdministrateur(Administrateur administrateur, Long mairieId);
    List<Administrateur> listerAdministrateurs();

    // ── Types de problèmes ────────────────────────────────────────
    TypeProbleme creerTypeProbleme(String nomType);
    List<TypeProbleme> listerTypesProblemes();

    // ── Super Admin ───────────────────────────────────────────────
    SuperAdmin ajouterSuperAdmin(SuperAdmin superAdmin);
    boolean existeSuperAdmin(String email);
}