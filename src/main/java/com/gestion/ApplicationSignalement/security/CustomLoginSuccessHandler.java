package com.gestion.ApplicationSignalement.security;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Cette classe permet de rediriger automatiquement
 * l'utilisateur vers le dashboard correspondant à son rôle
 * après une connexion réussie.
 */
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {

            String role = authority.getAuthority();

            if (role.equals("ROLE_CITOYEN")) {
                response.sendRedirect("/citoyen/dashboard");
                return;
            }

            if (role.equals("ROLE_ADMINISTRATEUR")) {
                response.sendRedirect("/administrateur/dashboard");
                return;
            }

            if (role.equals("ROLE_SUPERADMIN")) {
                response.sendRedirect("/superadmin/dashboard");
                return;
            }
        }

        // Si aucun rôle ne correspond
        response.sendRedirect("/login?error");
    }
}