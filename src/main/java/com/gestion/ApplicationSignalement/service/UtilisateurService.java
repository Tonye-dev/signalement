package com.gestion.ApplicationSignalement.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Utilisateur;


public interface UtilisateurService {
    
    // inscrire un nouvel utilisateur
    Citoyen inscrireCitoyen(Citoyen citoyen);

    // connexion, retrouver un utilisateur par son nom et som email
    Optional<Utilisateur> connecterUtilisateur(String email, String motdepasse);

    //trouver un utilisateur par email
    Optional<Utilisateur> trouverParEmail(String email);

  

    // modifierun utilisateur
    Utilisateur modifierUtilisateur(Utilisateur utilisateurModifie, Long id);

    // supprimer un utilisateur
   void supprimerUtilisateur(Long id);

   String updateProfilePhoto(Utilisateur utilisateur, MultipartFile file);

}
