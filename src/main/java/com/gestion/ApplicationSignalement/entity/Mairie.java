package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "mairies")
public class Mairie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @ElementCollection
    @CollectionTable(name = "mairie_quartiers", joinColumns = @JoinColumn(name = "mairie_id"))
    @Column(name = "quartier")
    private List<String> zoneIntervention;

    // ── Vitrine (tous optionnels) ──────────────────────────────────
    @Column(columnDefinition = "TEXT")
    private String description;
    private String photoPath;
    private String telephone;
    @Column(name = "email_officiel")
    private String emailOfficiel;
    @Column(columnDefinition = "TEXT")
    private String horaires;

    // ── Relations ─────────────────────────────────────────────────
    @OneToMany(mappedBy = "mairie")
    private List<Administrateur> administrateurs;

    @OneToMany(mappedBy = "mairie")
    private List<Probleme> problemes;

    @OneToMany(mappedBy = "mairie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceMunicipal> services;

    @OneToMany(mappedBy = "mairie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgentMunicipal> agents;

    @OneToMany(mappedBy = "mairie")
    private List<Annonce> annonces;

    public Mairie() {}

    public Mairie(String nom, String adresse, List<String> zoneIntervention) {
        this.nom = nom; this.adresse = adresse; this.zoneIntervention = zoneIntervention;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String n) { this.nom = n; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String a) { this.adresse = a; }
    public List<String> getZoneIntervention() { return zoneIntervention; }
    public void setZoneIntervention(List<String> z) { this.zoneIntervention = z; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String p) { this.photoPath = p; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String t) { this.telephone = t; }
    public String getEmailOfficiel() { return emailOfficiel; }
    public void setEmailOfficiel(String e) { this.emailOfficiel = e; }
    public String getHoraires() { return horaires; }
    public void setHoraires(String h) { this.horaires = h; }
    public List<Administrateur> getAdministrateurs() { return administrateurs; }
    public void setAdministrateurs(List<Administrateur> a) { this.administrateurs = a; }
    public List<Probleme> getProblemes() { return problemes; }
    public void setProblemes(List<Probleme> p) { this.problemes = p; }
    public List<ServiceMunicipal> getServices() { return services; }
    public void setServices(List<ServiceMunicipal> s) { this.services = s; }
    public List<AgentMunicipal> getAgents() { return agents; }
    public void setAgents(List<AgentMunicipal> a) { this.agents = a; }
    public List<Annonce> getAnnonces() { return annonces; }
    public void setAnnonces(List<Annonce> a) { this.annonces = a; }
}