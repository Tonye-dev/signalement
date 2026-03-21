package com.gestion.ApplicationSignalement.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.ApplicationSignalement.repository.CitoyenRepository;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;
import com.gestion.enums.Role;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final CitoyenRepository citoyenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // Injection correcte

    // ✅ Constructeur avec injection de tous les services
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                  CitoyenRepository citoyenRepository,
                                  PasswordEncoder passwordEncoder,
                                  EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.citoyenRepository = citoyenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // Inscription d'un utilisateur
    @Override
    public Citoyen inscrireCitoyen(Citoyen citoyen) {
        System.out.println("INSCRIPTION SERVICE: email = " + citoyen.getEmail());

        // Validation des données
        if (citoyen.getEmail() == null || citoyen.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email obligatoire");
        }
        if (citoyen.getMotdepasse() == null || citoyen.getMotdepasse().isBlank()) {
            throw new IllegalArgumentException("Mot de passe obligatoire");
        }

        // Vérifier si email existe déjà
        if (utilisateurRepository.existsByEmail(citoyen.getEmail())) {
            throw new IllegalStateException("Email déjà utilisé");
        }

        // Logique métier
        citoyen.setMotdepasse(passwordEncoder.encode(citoyen.getMotdepasse()));
        citoyen.setRole(Role.CITOYEN);
        citoyen.setValide(false);
        citoyen.setVerificationToken(UUID.randomUUID().toString());
        citoyen.setTokenExpiration(LocalDateTime.now().plusHours(24));

        Citoyen saved = citoyenRepository.save(citoyen);

        // Envoi de l'email de vérification
      
         emailService.envoyerEmailVerification(saved);

        System.out.println("Utilisateur enregistré : " + saved.getEmail());
        return saved;
    }

    // Connexion d'un utilisateur
    @Override
    public Optional<Utilisateur> connecterUtilisateur(String email, String motdepasse) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            if (utilisateur.isValide() && passwordEncoder.matches(motdepasse, utilisateur.getMotdepasse())) {
                return Optional.of(utilisateur);
            }
        }
        return Optional.empty();
    }

    // Trouver un utilisateur par email
    @Override
    public Optional<Utilisateur> trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    // Modifier un utilisateur
    @Override
    public Utilisateur modifierUtilisateur(Utilisateur utilisateurmodifie, Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        utilisateur.setNom(utilisateurmodifie.getNom());
        utilisateur.setPrenom(utilisateurmodifie.getPrenom());
        utilisateur.setMotdepasse(passwordEncoder.encode(utilisateurmodifie.getMotdepasse()));

        return utilisateurRepository.save(utilisateur);
    }

    // Supprimer un utilisateur
    @Override
    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    // Modifier la photo de profil d'un utilisateur
    @Override
    public String updateProfilePhoto(Utilisateur utilisateur, MultipartFile file) {
        try {
            if (file.isEmpty()) throw new RuntimeException("Fichier vide");
            if (!file.getContentType().startsWith("image/")) throw new RuntimeException("Type de fichier non autorisé");

            Path root = Paths.get("uploads/profiles");
            if (!Files.exists(root)) Files.createDirectories(root);

            String extension = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = "user_" + utilisateur.getId() + "_" + System.currentTimeMillis() + extension;

            Path destination = root.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            utilisateur.setPhotoPath(destination.toString());
            utilisateurRepository.save(utilisateur);

            return destination.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la photo", e);
        }
    }
}
