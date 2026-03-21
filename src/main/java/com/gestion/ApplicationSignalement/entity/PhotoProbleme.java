package com.gestion.ApplicationSignalement.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="photos_problemes")
public class PhotoProbleme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomFichier;
    private String chemin;
    private LocalDate dateAjout;

    @ManyToOne
    @JoinColumn(name = "probleme_id")
    private Probleme probleme;

    public PhotoProbleme() {}

    public PhotoProbleme(String nomFichier, String chemin, Probleme probleme){
        this.nomFichier = nomFichier;
        this.chemin = chemin;
        this.probleme = probleme;
    }

    public Long getId() { return id; }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public String getChemin() { return chemin; }
    public void setChemin(String chemin) { this.chemin = chemin; }

    public Probleme getProbleme() { return probleme; }
    public void setProbleme(Probleme probleme) { this.probleme = probleme; }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    
}
