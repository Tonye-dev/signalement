

package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.PhotoProbleme;
import com.gestion.ApplicationSignalement.entity.Probleme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoProblemeRepository extends JpaRepository<PhotoProbleme, Long> {

    List<PhotoProbleme> findByProbleme(Probleme probleme);

    List<PhotoProbleme> findByProblemeOrderByDateAjoutAsc(Probleme probleme);
}

