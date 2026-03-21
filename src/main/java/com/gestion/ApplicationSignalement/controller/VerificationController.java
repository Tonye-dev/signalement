package com.gestion.ApplicationSignalement.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;

/**
 * VerificationController
 * ─────────────────────────────────────────────────────────────
 * Gère la confirmation du compte via le lien envoyé par email.
 *
 * Flux complet :
 *   1. L'email contient un lien : /api/verification/confirm?token=xxx
 *   2. Ce contrôleur valide le token et active le compte
 *   3. Il redirige vers /activation?status=success  (ou error/expired/already)
 *   4. ActivationController (@Controller) sert la page activation.html
 *   5. activation.html affiche le bon message et redirige vers /login
 * ─────────────────────────────────────────────────────────────
 */
@Controller                          // @Controller (pas @RestController) pour pouvoir faire redirect:
@RequestMapping("/api/verification")
public class VerificationController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * GET /api/verification/confirm?token=xxx
     * Appelé directement depuis le lien dans l'email de l'utilisateur.
     */
    @GetMapping("/confirm")
    public String confirmerUtilisateur(@RequestParam String token) {

        // 1. Chercher l'utilisateur par token
        Utilisateur utilisateur = utilisateurRepository
                .findByVerificationToken(token)
                .orElse(null);

        // Token introuvable ou invalide
        if (utilisateur == null) {
            return "redirect:/activation?status=error";
        }

        // Token expiré
        if (utilisateur.getTokenExpiration() != null &&
            utilisateur.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return "redirect:/activation?status=expired";
        }

        // Compte déjà activé
        if (utilisateur.isValide()) {
            return "redirect:/activation?status=already";
        }

        // Activation du compte
        utilisateur.setValide(true);
        utilisateur.setVerificationToken(null);   // supprimer le token usagé
        utilisateur.setTokenExpiration(null);
        utilisateurRepository.save(utilisateur);

        return "redirect:/activation?status=success";
    }
}