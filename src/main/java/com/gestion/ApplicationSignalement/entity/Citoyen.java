package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "citoyens")
public class Citoyen extends Utilisateur {

    @NotBlank
    private String adresse;

    

   
    
    @NotBlank
    private String telephone;


    @ManyToMany
    @JoinTable(
        name = "citoyen_probleme",
        joinColumns = @JoinColumn(name = "citoyen_id"),
        inverseJoinColumns = @JoinColumn(name = "probleme_id")
    )
    private List<Probleme> problemessignales;

  

    public Citoyen() {}

    public Citoyen(String adresse, String telephone){
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public List<Probleme> getProblemessignales() { return problemessignales; }
    public void setProblemessignales(List<Probleme> problemessignales) { this.problemessignales = problemessignales; }

  

    
    
}
