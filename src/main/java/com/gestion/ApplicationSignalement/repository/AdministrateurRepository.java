package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Mairie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {

    // --- Chercher un administrateur par email ---
    Optional<Administrateur> findByEmail(String email);

    // --- Chercher un administrateur par nom ---
    Optional<Administrateur> findByNom(String nom);

    // --- Vérifier si un administrateur existe par nom ---
    boolean existsByNom(String nom);

    // --- Récupérer tous les administrateurs d'une mairie ---
    List<Administrateur> findByMairie(Mairie mairie);

    // --- findById est déjà fourni par JpaRepository, donc inutile de le redéfinir ---
}