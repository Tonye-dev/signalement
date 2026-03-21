
package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Mairie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MairieRepository extends JpaRepository<Mairie, Long> {

    // Exemple de méthode personnalisée pour chercher un utilisateur par email
    
    Optional<Mairie> findByNom (String nom);
   
    boolean existsByNom(String nom);
    Optional<Mairie> findById(Long id);
 
}
