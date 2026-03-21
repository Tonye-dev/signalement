package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sous_services_municipaux")
public class SousServiceMunicipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceMunicipal service;

    public SousServiceMunicipal() {}

    public SousServiceMunicipal(String nom, String description, ServiceMunicipal service) {
        this.nom = nom; this.description = description; this.service = service;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String n) { this.nom = n; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public ServiceMunicipal getService() { return service; }
    public void setService(ServiceMunicipal s) { this.service = s; }
}