package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.ServiceMunicipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceMunicipalRepository extends JpaRepository<ServiceMunicipal, Long> {
    List<ServiceMunicipal> findByMairieOrderByOrdreAsc(Mairie mairie);
}