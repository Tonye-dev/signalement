package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


import com.gestion.enums.Role;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Exemple de méthode personnalisée pour chercher un utilisateur par role
    

     List<Utilisateur> findByRole (String role);
     
     Optional<Utilisateur>  findByEmail(String email);
     
     boolean existsByEmail(String email);

     List<Utilisateur> findByValideFalse();

    Optional<Utilisateur> findByVerificationToken(String token);

     long countByRole(Role role);
 
}
