package com.gestion.dto;

public class LoginRequestDto {
    private String email;
    private String motdepassse;
    


    

     
     public String getEmail(){
        return email;
     }
     public void setEmail(String email){
        this.email = email;
     }

     public String getMotdepasse(){return motdepassse;}

     public void setMotdepasse(String motdepasse){
        this.motdepassse = motdepasse;
     }


}
