package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.enums.Statut;

public interface EmailService {

    // Envoi email de vérification pour un utilisateur
    void envoyerEmailVerification(Utilisateur utilisateur);

    // Envoi email aux citoyens lors du changement de statut d'un problème
    void envoyerEmailNotification(Probleme probleme, Statut ancienStatut);
}