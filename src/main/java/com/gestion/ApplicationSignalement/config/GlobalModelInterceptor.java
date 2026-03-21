package com.gestion.ApplicationSignalement.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Intercepteur global qui injecte automatiquement le rôle
 * de l'utilisateur connecté dans chaque modèle Thymeleaf.
 *
 * Cela permet d'utiliser th:if="${role == 'CITOYEN'}" dans
 * le layout sans avoir à le passer manuellement dans chaque contrôleur.
 */
@Component
public class GlobalModelInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {

        if (modelAndView == null) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return;

        // Extraire le rôle (ex: ROLE_CITOYEN → CITOYEN)
        auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .findFirst()
                .ifPresent(roleWithPrefix -> {
                    String role = roleWithPrefix.replace("ROLE_", "");
                    modelAndView.addObject("role", role);
                });
    }
}