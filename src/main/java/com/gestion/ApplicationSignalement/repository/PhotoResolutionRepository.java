package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.PhotoResolution;
import com.gestion.ApplicationSignalement.entity.Probleme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoResolutionRepository extends JpaRepository<PhotoResolution, Long> {
    List<PhotoResolution> findByProblemeOrderByDateAjoutAsc(Probleme probleme);
}