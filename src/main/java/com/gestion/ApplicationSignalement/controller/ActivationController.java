package com.gestion.ApplicationSignalement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ActivationController
 * ──────────────────────────────────────────────────────────────
 * Sert simplement la page activation.html (Thymeleaf).
 * Le paramètre ?status= est lu directement côté HTML via ${param.status}
 * ──────────────────────────────────────────────────────────────
 */
@Controller
public class ActivationController {

    @GetMapping("/activation")
    public String activationPage() {
        return "activation";   // → templates/activation.html
    }
}