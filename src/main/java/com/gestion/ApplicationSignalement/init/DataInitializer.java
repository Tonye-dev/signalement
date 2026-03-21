package com.gestion.ApplicationSignalement.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.SuperAdmin;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.enums.Role;
import com.gestion.ApplicationSignalement.service.AdministrateurService;
import com.gestion.ApplicationSignalement.service.SuperAdminService;

@Component
@Profile("!test")
public class DataInitializer {

    @Autowired
    private AdministrateurService administrateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SuperAdminService superAdminService;

    @PostConstruct
    public void initSuperAdmin() {
        String email = "kisitolecosto@gmail.com";

        if (!superAdminService.existeSuperAdmin(email)) {
            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setNom("Tonye");
            superAdmin.setPrenom("Emmanuel");
            superAdmin.setEmail(email);
            superAdmin.setMotdepasse(passwordEncoder.encode("Merciseigneur"));
            superAdmin.setRole(Role.SUPERADMIN);
            superAdmin.setValide(true);

            superAdminService.ajouterSuperAdmin(superAdmin);
            System.out.println("Super administrateur créé : " + email);
        } else {
            System.out.println("Super administrateur déjà présent : " + email);
        }
    }
}