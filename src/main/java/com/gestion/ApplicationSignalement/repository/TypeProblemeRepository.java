
package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TypeProblemeRepository extends JpaRepository<TypeProbleme, Long> {


    List<TypeProbleme> findByNomType(String nomType);
    boolean existsByNomTypeContainingIgnoreCase(String nomType);
    

 
}