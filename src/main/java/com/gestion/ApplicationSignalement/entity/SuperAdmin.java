package com.gestion.ApplicationSignalement.entity;

import com.gestion.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "superadmins")
public class SuperAdmin extends Utilisateur {

    public SuperAdmin() {}

    public SuperAdmin(String nom, String prenom, String email, String motdepasse) {
        super(nom, Role.SUPERADMIN, prenom, email, motdepasse);
    }

}
