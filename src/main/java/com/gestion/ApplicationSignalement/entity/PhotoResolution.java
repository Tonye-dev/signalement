package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos_resolution")
public class PhotoResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_photo_resolution")
    private Long id;

    @Column(name = "nom_fichier")
    private String nomFichier;

    @Column(name = "chemin")
    private String chemin;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "probleme_id", nullable = false)
    private Probleme probleme;

    @ManyToOne
    @JoinColumn(name = "administrateur_id", nullable = false)
    private Administrateur administrateur;

    public PhotoResolution() {}

    public PhotoResolution(String nomFichier, String chemin,
                           Probleme probleme, Administrateur administrateur) {
        this.nomFichier      = nomFichier;
        this.chemin          = chemin;
        this.probleme        = probleme;
        this.administrateur  = administrateur;
        this.dateAjout       = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String n) { this.nomFichier = n; }
    public String getChemin() { return chemin; }
    public void setChemin(String c) { this.chemin = c; }
    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime d) { this.dateAjout = d; }
    public Probleme getProbleme() { return probleme; }
    public void setProbleme(Probleme p) { this.probleme = p; }
    public Administrateur getAdministrateur() { return administrateur; }
    public void setAdministrateur(Administrateur a) { this.administrateur = a; }
}