package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.AgentMunicipal;
import com.gestion.ApplicationSignalement.entity.Mairie;
import com.gestion.ApplicationSignalement.entity.ServiceMunicipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgentMunicipalRepository extends JpaRepository<AgentMunicipal, Long> {
    List<AgentMunicipal> findByMairieOrderByPosteAsc(Mairie mairie);
    List<AgentMunicipal> findByService(ServiceMunicipal service);
}