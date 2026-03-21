package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "agents_municipaux")
public class AgentMunicipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    private LocalDate dateNaissance;

    @Column(nullable = false)
    private String poste; // ex: "Chef du service État Civil"

    private LocalDate datePriseDeFonction;

    private String photoPath;

    @ManyToOne
    @JoinColumn(name = "mairie_id", nullable = false)
    private Mairie mairie;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceMunicipal service; // optionnel

    public AgentMunicipal() {}

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String n) { this.nom = n; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String p) { this.prenom = p; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate d) { this.dateNaissance = d; }
    public String getPoste() { return poste; }
    public void setPoste(String p) { this.poste = p; }
    public LocalDate getDatePriseDeFonction() { return datePriseDeFonction; }
    public void setDatePriseDeFonction(LocalDate d) { this.datePriseDeFonction = d; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String p) { this.photoPath = p; }
    public Mairie getMairie() { return mairie; }
    public void setMairie(Mairie m) { this.mairie = m; }
    public ServiceMunicipal getService() { return service; }
    public void setService(ServiceMunicipal s) { this.service = s; }
}