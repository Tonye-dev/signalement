package com.gestion.dto;

import java.time.LocalDate;


public class PhotoProblemeDto {
    
    private String url; // chemin ou URL publique
    private LocalDate dateAjout;
  
  
    public PhotoProblemeDto(String url, LocalDate dateAjout) {
        this.url = url;
        this.dateAjout = dateAjout;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public LocalDate getDateAjout() {
        return dateAjout;
    }


    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }





    
}
