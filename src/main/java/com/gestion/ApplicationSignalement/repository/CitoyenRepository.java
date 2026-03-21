package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.enums.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List; 

@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Long> {

    // Exemple de méthode personnalisée pour chercher un utilisateur par email
    Optional<Citoyen> findByEmail(String email);
    List<Citoyen> findByNomContainingIgnoreCase (String nom);
    Optional<Citoyen> findById(Long id);
    boolean existsByEmail(String email);
    List<Citoyen> findByProblemessignales(Probleme probleme);
    List<Utilisateur>findByValideFalseAndRole(Role role);

    Optional<Utilisateur> findByVerificationToken(String token);
 
}
