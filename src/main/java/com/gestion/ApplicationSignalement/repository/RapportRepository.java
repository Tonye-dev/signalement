package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.ApplicationSignalement.entity.Mairie;


import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {

    // Exemple de méthode personnalisée pour chercher un rapport par mairie 
    
    List<Rapport> findByMairie(Mairie mairie);
    

 
}