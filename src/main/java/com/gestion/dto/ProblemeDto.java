package com.gestion.dto;

import java.util.List;

import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.enums.Statut;

public class ProblemeDto {
    
    private String titre;
    private String description;
    private TypeProbleme typeProbleme;
    private String quartier;
    private Statut statut;
    private List<PhotoProblemeDto> photos;


    


    public ProblemeDto(String titre, String description, TypeProbleme typeProbleme, String quartier, Statut statut,
            List<PhotoProblemeDto> photos) {
        this.titre = titre;
        this.description = description;
        this.typeProbleme = typeProbleme;
        this.quartier = quartier;
        this.statut = statut;
        this.photos = photos;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public TypeProbleme getTypeProbleme() {
        return typeProbleme;
    }
    public void setTypeProbleme(TypeProbleme typeProbleme) {
        this.typeProbleme = typeProbleme;
    }
    public String getLocalisation() {
        return quartier;
    }
    public void setLocalisation(String localisation) {
        this.quartier = localisation;
    }
    public List<PhotoProblemeDto> getPhotos() {
        return photos;
    }
    public void setPhotos(List<PhotoProblemeDto> photos) {
        this.photos = photos;
    }

    
}
