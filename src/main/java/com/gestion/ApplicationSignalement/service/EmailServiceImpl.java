package com.gestion.ApplicationSignalement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.enums.Statut;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envoie l'email de vérification de compte à un utilisateur
     */
    @Override
    public void envoyerEmailVerification(Utilisateur utilisateur) {

        String lienVerification = "http://localhost:8080/api/verification/confirm?token=" + utilisateur.getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(utilisateur.getEmail());
        message.setSubject("Vérification de votre compte ASPU");
        message.setText(
                "Bonjour " + utilisateur.getNom() + ",\n\n" +
                "Bienvenue sur ASPU.\n" +
                "Afin d'activer votre compte, veuillez cliquer sur le lien ci-dessous :\n\n" +
                lienVerification + "\n\n" +
                "Ce lien est valide pendant 24 heures.\n\n" +
                "Cordialement,\n" +
                "L'équipe ASPU"
        );

        mailSender.send(message);
    }

    /**
     * Envoie un email de notification à tous les citoyens
     * lorsque le statut d'un problème change
     * @param probleme le problème dont le statut a changé
     * @param ancienStatut l'ancien statut du problème
     */
    @Override
    public void envoyerEmailNotification(Probleme probleme, Statut ancienStatut) {

        if (probleme.getCitoyens() == null || probleme.getCitoyens().isEmpty()) {
            return; // Aucun citoyen à notifier
        }

        String sujet = "Mise à jour de votre signalement";

        for (Citoyen citoyen : probleme.getCitoyens()) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(citoyen.getEmail());
            mail.setSubject(sujet);
            mail.setText(String.format(
                    "Bonjour %s,\n\n" +
                    "Le statut de votre problème a changé.\n\n" +
                    "Ancien statut : %s\n" +
                    "Nouveau statut : %s\n\n" +
                    "Vous pouvez consulter les détails dans votre espace personnel.\n\n" +
                    "Merci pour votre contribution citoyenne.",
                    citoyen.getNom(),
                    ancienStatut,
                    probleme.getStatut()
            ));

            mailSender.send(mail);
        }
    }
}