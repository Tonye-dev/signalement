package com.gestion.ApplicationSignalement.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gestion.ApplicationSignalement.entity.Utilisateur;

/**
 * Classe UserDetails personnalisée pour Spring Security.
 * Permet de gérer les rôles (ROLE_ADMIN / ROLE_CITOYEN)
 * et de contrôler l'accès aux endpoints sécurisés.
 */
public class CustomerUserDetails implements UserDetails {

    private final Utilisateur utilisateur;

    public CustomerUserDetails(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    /**
     * Retourne les rôles de l'utilisateur avec le préfixe "ROLE_"
     * pour que Spring Security reconnaisse les @PreAuthorize("hasRole('ADMIN')")
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return utilisateur.getMotdepasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    /**
     * Toujours vrai pour ce projet (compte jamais expiré)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Toujours vrai pour ce projet (compte jamais bloqué)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Retourne true si le compte est valide
     * (tu utilises ce champ pour activer/désactiver un utilisateur)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Compte activé uniquement si valide = true
     */
    @Override
    public boolean isEnabled() {
        return utilisateur.isValide();
    }

    /**
     * Permet de récupérer l'utilisateur complet si besoin
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
