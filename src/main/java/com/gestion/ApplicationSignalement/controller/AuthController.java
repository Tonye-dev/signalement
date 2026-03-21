package com.gestion.ApplicationSignalement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.ApplicationSignalement.config.jwt.JwtUtil;
import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;
import com.gestion.ApplicationSignalement.service.AdministrateurService;
import com.gestion.ApplicationSignalement.service.CustomerUserDetailsService;
import com.gestion.ApplicationSignalement.service.SuperAdminService;
import com.gestion.ApplicationSignalement.service.UtilisateurService;
import com.gestion.dto.LoginRequestDto;
import com.gestion.dto.UtilisateurDto;
import com.gestion.enums.Role;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UtilisateurService utilisateurService;
    private final AdministrateurService administrateurService;
    private final SuperAdminService superAdminService;
    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;

    public AuthController(
            PasswordEncoder passwordEncoder,
            UtilisateurService utilisateurService,
            AdministrateurService administrateurService,
            SuperAdminService superAdminService,
            AuthenticationManager authenticationManager,
            CustomerUserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UtilisateurRepository utilisateurRepository) {

        this.passwordEncoder       = passwordEncoder;
        this.utilisateurService    = utilisateurService;
        this.administrateurService = administrateurService;
        this.superAdminService     = superAdminService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService    = userDetailsService;
        this.jwtUtil               = jwtUtil;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ══════════════════════════════════════════════════════════════
    //  INSCRIPTION CITOYEN
    //  POST /api/auth/inscription/citoyen
    // ══════════════════════════════════════════════════════════════
    @PostMapping("/inscription/citoyen")
    public ResponseEntity<?> inscrireCitoyen(
            @RequestPart("citoyen") @Valid String citoyenJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        // ── 1. Parser le JSON du citoyen ──────────────────────────
        ObjectMapper mapper = new ObjectMapper();
        Citoyen citoyen;
        try {
            citoyen = mapper.readValue(citoyenJson, Citoyen.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Données invalides : " + e.getMessage());
        }

        // ── 2. Vérifications métier de base ───────────────────────
        if (citoyen.getEmail() == null || citoyen.getEmail().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("L'adresse email est obligatoire.");
        }

        if (citoyen.getMotdepasse() == null || citoyen.getMotdepasse().length() < 6) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Le mot de passe doit contenir au moins 6 caractères.");
        }

        citoyen.setRole(Role.CITOYEN);

        // ── 3. Enregistrement en base ─────────────────────────────
        Citoyen saved;
        try {
            saved = utilisateurService.inscrireCitoyen(citoyen);

        } catch (IllegalStateException e) {
            // Email déjà utilisé → 409 Conflict
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Cette adresse email est déjà associée à un compte.");

        } catch (Exception e) {
            // Erreur inattendue (base de données, contrainte, etc.)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'inscription. Veuillez réessayer.");
        }

        // ── 4. Sauvegarde de la photo (non bloquante) ─────────────
        if (photo != null && !photo.isEmpty()) {
            try {
                utilisateurService.updateProfilePhoto(saved, photo);
            } catch (Exception e) {
                // La photo n'est pas critique : l'inscription continue normalement
                System.err.println("[AuthController] Photo non sauvegardée : " + e.getMessage());
            }
        }

        // ── 5. Retourner le DTO de confirmation ───────────────────
        UtilisateurDto dto = new UtilisateurDto();
        dto.setNom(saved.getNom());
        dto.setPrenom(saved.getPrenom());
        dto.setEmail(saved.getEmail());
        dto.setRole(saved.getRole().name());
        dto.setValide(saved.isValide()); // false → email de confirmation en attente

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // ══════════════════════════════════════════════════════════════
    //  CONNEXION
    //  POST /api/auth/connexion
    // ══════════════════════════════════════════════════════════════
    @PostMapping("/connexion")
    public ResponseEntity<?> connecterUtilisateur(@RequestBody LoginRequestDto loginRequest) {

        // ── 1. Vérifications de base ──────────────────────────────
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("L'adresse email est obligatoire.");
        }

        if (loginRequest.getMotdepasse() == null || loginRequest.getMotdepasse().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Le mot de passe est obligatoire.");
        }

        // ── 2. Vérifier que le compte existe ──────────────────────
        Utilisateur utilisateur;
        try {
            utilisateur = utilisateurRepository
                    .findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Compte introuvable"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Aucun compte trouvé avec cette adresse email.");
        }

        // ── 3. Vérifier que le compte est activé ──────────────────
        if (!utilisateur.isValide()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Votre compte n'est pas encore activé. " +
                          "Vérifiez votre boîte email et cliquez sur le lien de confirmation.");
        }

        // ── 4. Authentifier via Spring Security ───────────────────
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getMotdepasse()));

        } catch (BadCredentialsException e) {
            // Mauvais mot de passe
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Mot de passe incorrect.");

        } catch (DisabledException e) {
            // Compte désactivé côté Spring Security
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Votre compte a été désactivé. Contactez l'administrateur.");

        } catch (LockedException e) {
            // Compte verrouillé
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Votre compte est temporairement verrouillé. Réessayez plus tard.");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la connexion. Veuillez réessayer.");
        }

        // ── 5. Générer le token JWT ────────────────────────────────
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // ── 6. Construire et retourner le DTO ─────────────────────
        UtilisateurDto dto = new UtilisateurDto();
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole().name());
        dto.setValide(utilisateur.isValide());
        dto.setToken(jwt);

        return ResponseEntity.ok(dto);
    }
}