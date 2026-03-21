package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.gestion.enums.Statut;

@Entity
@Table(name = "problemes")
public class Probleme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_probleme")
    private Long id;

    private String titre;
    private String description;
    private String quartier;

    @Column(name = "date_signalement")
    private LocalDateTime dateSignalement;

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution; // ← nouveau : rempli automatiquement à la résolution

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.SIGNALE;

    @ManyToOne
    @JoinColumn(name = "mairie_id")
    private Mairie mairie;

    @ManyToOne
    @JoinColumn(name = "idtype")
    private TypeProbleme typeProbleme;

    @ManyToMany
    @JoinTable(
        name = "probleme_citoyen",
        joinColumns = @JoinColumn(name = "probleme_id"),
        inverseJoinColumns = @JoinColumn(name = "citoyen_id")
    )
    private List<Citoyen> citoyens;

    // Photos du citoyen (signalement)
    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoProbleme> photos;

    // Photos de résolution ajoutées par l'admin
    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoResolution> photosResolution; // ← nouveau

    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rapport> rapports;

    @ManyToOne
    @JoinColumn(name = "administrateur_id")
    private Administrateur administrateur;

    public Probleme() {}

    public Probleme(String titre, String description, String quartier,
                    TypeProbleme typeProbleme, Mairie mairie) {
        this.titre           = titre;
        this.description     = description;
        this.quartier        = quartier;
        this.typeProbleme    = typeProbleme;
        this.mairie          = mairie;
        this.dateSignalement = LocalDateTime.now();
        this.statut          = Statut.SIGNALE;
    }

    /**
     * Calcule le temps de résolution en heures.
     * Retourne null si le problème n'est pas encore résolu.
     */
    public Long getTempsResolutionHeures() {
        if (dateSignalement == null || dateResolution == null) return null;
        return ChronoUnit.HOURS.between(dateSignalement, dateResolution);
    }

    /**
     * Retourne le temps de résolution formaté en lisible.
     * Ex: "3 jours 5h" ou "12h"
     */
    public String getTempsResolutionFormate() {
        Long heures = getTempsResolutionHeures();
        if (heures == null) return "—";
        if (heures < 24) return heures + "h";
        long jours = heures / 24;
        long reste = heures % 24;
        return reste > 0 ? jours + "j " + reste + "h" : jours + " jour(s)";
    }

    // ── Getters & Setters ──────────────────────────────────────────
    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getQuartier() { return quartier; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public LocalDateTime getDateSignalement() { return dateSignalement; }
    public void setDateSignalement(LocalDateTime d) { this.dateSignalement = d; }
    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime d) { this.dateResolution = d; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public Mairie getMairie() { return mairie; }
    public void setMairie(Mairie mairie) { this.mairie = mairie; }
    public TypeProbleme getTypeProbleme() { return typeProbleme; }
    public void setTypeProbleme(TypeProbleme t) { this.typeProbleme = t; }
    public List<Citoyen> getCitoyens() { return citoyens; }
    public void setCitoyens(List<Citoyen> citoyens) { this.citoyens = citoyens; }
    public List<PhotoProbleme> getPhotos() { return photos; }
    public void setPhotos(List<PhotoProbleme> photos) { this.photos = photos; }
    public List<PhotoResolution> getPhotosResolution() { return photosResolution; }
    public void setPhotosResolution(List<PhotoResolution> p) { this.photosResolution = p; }
    public List<Rapport> getRapports() { return rapports; }
    public void setRapports(List<Rapport> rapports) { this.rapports = rapports; }
    public Administrateur getAdministrateur() { return administrateur; }
    public void setAdministrateur(Administrateur a) { this.administrateur = a; }
}