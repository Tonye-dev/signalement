package com.gestion.ApplicationSignalement.entity;

import java.time.LocalDateTime;

import com.gestion.enums.Role;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String prenom;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String motdepasse;

    
    @Column(nullable = false)
    private boolean valide = false;


  
    @Column(name = "verification_token")
    private String verificationToken = null;

    @Column(name = "tokenExpiration")
    private LocalDateTime tokenExpiration;

   
    private String photoPath;

    public Utilisateur() {}


    public Utilisateur(String nom, Role role, String prenom, String email, String motdepasse){
        this.nom = nom;
        this.role= role;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.valide = false;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }


    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotdepasse() { return motdepasse; }
    public void setMotdepasse(String motdepasse) { this.motdepasse = motdepasse; }

    public boolean isValide(){return valide;}
    public void setValide(boolean valide){this.valide = valide;}



    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    
  public String getVerificationToken() {
        return verificationToken;
    }


    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }


     public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }



}
