package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "typeproblemes")
public class TypeProbleme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtype")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nomType;

    private String descriptionType;

    @OneToMany(mappedBy = "typeProbleme")
    private List<Probleme> problemetype;

    // --- Constructeur vide obligatoire pour JPA ---
    public TypeProbleme() {}

    // --- Constructeur pratique pour créer un type avec nom uniquement ---
    public TypeProbleme(String nomType) {
        this.nomType = nomType;
    }

    // --- Constructeur complet si besoin ---
    public TypeProbleme(String nomType, String descriptionType){
        this.nomType = nomType;
        this.descriptionType = descriptionType;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public String getNomType() { return nomType; }
    public void setNomType(String nomType) { this.nomType = nomType; }

    public String getDescriptionType() { return descriptionType; }
    public void setDescriptionType(String descriptionType) { this.descriptionType = descriptionType; }

    public List<Probleme> getProblemetype() { return problemetype; }
    public void setProblemetype(List<Probleme> problemetype) { this.problemetype = problemetype; }
}