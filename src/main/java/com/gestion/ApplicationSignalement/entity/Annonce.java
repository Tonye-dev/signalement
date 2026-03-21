package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_annonce")
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieAnnonce categorie;

    private boolean estArrete = false; // true si arrêté officiel

    @Column(name = "date_publication")
    private LocalDateTime datePublication = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "mairie_id", nullable = false)
    private Mairie mairie;

    @ManyToOne
    @JoinColumn(name = "administrateur_id", nullable = false)
    private Administrateur administrateur;

    public Annonce() {}

    public Annonce(String titre, String contenu, CategorieAnnonce categorie,
                   Mairie mairie, Administrateur administrateur) {
        this.titre           = titre;
        this.contenu         = contenu;
        this.categorie       = categorie;
        this.mairie          = mairie;
        this.administrateur  = administrateur;
        this.datePublication = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public void setTitre(String t) { this.titre = t; }
    public String getContenu() { return contenu; }
    public void setContenu(String c) { this.contenu = c; }
    public CategorieAnnonce getCategorie() { return categorie; }
    public void setCategorie(CategorieAnnonce c) { this.categorie = c; }
    public boolean isEstArrete() { return estArrete; }
    public void setEstArrete(boolean e) { this.estArrete = e; }
    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime d) { this.datePublication = d; }
    public Mairie getMairie() { return mairie; }
    public void setMairie(Mairie m) { this.mairie = m; }
    public Administrateur getAdministrateur() { return administrateur; }
    public void setAdministrateur(Administrateur a) { this.administrateur = a; }
}