package com.gestion.ApplicationSignalement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.SuperAdmin;

import java.util.Optional;
import java.util.List;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {

    // Cherche un superadmin par email
    Optional<SuperAdmin> findByEmail(String email);

    // Liste tous les superadmins
   
    
    // Vérifie si un superadmin existe avec cet email
    boolean existsByEmail(String email);
}
