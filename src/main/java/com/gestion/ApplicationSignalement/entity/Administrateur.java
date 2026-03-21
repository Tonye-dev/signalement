package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.gestion.enums.Role;

@Entity
@Table(name = "administrateurs")
public class Administrateur extends Utilisateur {

    // --- Relation avec la mairie ---
    @ManyToOne
    @JoinColumn(name = "mairie_id", nullable = false)
    private Mairie mairie;

    // --- Relation avec les problèmes gérés ---
    @OneToMany(mappedBy = "administrateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Probleme> problemes = new ArrayList<>();

    // --- Relation avec les rapports générés ---
    @OneToMany(mappedBy = "administrateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rapport> rapports = new ArrayList<>();

    // --- Constructeurs ---
    public Administrateur() {}

    public Administrateur(String nom, String prenom, String email, String motdepasse, Mairie mairie) {
        super(nom, Role.ADMINISTRATEUR, prenom, email, motdepasse);
        this.mairie = mairie;
    }

    // --- Getters & Setters ---
    public Mairie getMairie() { return mairie; }
    public void setMairie(Mairie mairie) { this.mairie = mairie; }

    public List<Probleme> getProblemes() { return problemes; }
    public void setProblemes(List<Probleme> problemes) { this.problemes = problemes; }

    public List<Rapport> getRapports() { return rapports; }
    public void setRapports(List<Rapport> rapports) { this.rapports = rapports; }
}