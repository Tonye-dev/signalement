package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "services_municipaux")
public class ServiceMunicipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String icone;   // ex: "bi-file-person"
    private Integer ordre = 0;

    @ManyToOne
    @JoinColumn(name = "mairie_id", nullable = false)
    private Mairie mairie;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SousServiceMunicipal> sousServices;

    @OneToMany(mappedBy = "service")
    private List<AgentMunicipal> agents;

    public ServiceMunicipal() {}

    public ServiceMunicipal(String nom, String description, String icone, Mairie mairie) {
        this.nom = nom; this.description = description;
        this.icone = icone; this.mairie = mairie;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String n) { this.nom = n; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getIcone() { return icone; }
    public void setIcone(String i) { this.icone = i; }
    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer o) { this.ordre = o; }
    public Mairie getMairie() { return mairie; }
    public void setMairie(Mairie m) { this.mairie = m; }
    public List<SousServiceMunicipal> getSousServices() { return sousServices; }
    public void setSousServices(List<SousServiceMunicipal> s) { this.sousServices = s; }
    public List<AgentMunicipal> getAgents() { return agents; }
    public void setAgents(List<AgentMunicipal> a) { this.agents = a; }
}