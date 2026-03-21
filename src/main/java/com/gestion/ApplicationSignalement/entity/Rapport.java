package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapports")
public class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rapport") // ← corrigé : nom réel en base
    private Long id;

    @NotBlank
    private String contenu;

    @ManyToOne
    @JoinColumn(name = "probleme_id", nullable = false)
    private Probleme probleme;

    @ManyToOne
    @JoinColumn(name = "administrateur_id", nullable = false)
    private Administrateur administrateur;

    @ManyToOne
    @JoinColumn(name = "mairie_id", nullable = false)
    private Mairie mairie;

    @Column(name = "date_rapport") // ← corrigé : nom réel en base
    private LocalDateTime dateRapport;

    public Rapport() {}

    public Rapport(String contenu, Probleme probleme, Administrateur administrateur,
                   Mairie mairie, LocalDateTime dateRapport) {
        this.contenu       = contenu;
        this.probleme      = probleme;
        this.administrateur = administrateur;
        this.mairie        = mairie;
        this.dateRapport   = dateRapport;
    }

    public Long getId() { return id; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public Probleme getProbleme() { return probleme; }
    public void setProbleme(Probleme probleme) { this.probleme = probleme; }
    public Administrateur getAdministrateur() { return administrateur; }
    public void setAdministrateur(Administrateur administrateur) { this.administrateur = administrateur; }
    public Mairie getMairie() { return mairie; }
    public void setMairie(Mairie mairie) { this.mairie = mairie; }
    public LocalDateTime getDateRapport() { return dateRapport; }
    public void setDateRapport(LocalDateTime dateRapport) { this.dateRapport = dateRapport; }
}