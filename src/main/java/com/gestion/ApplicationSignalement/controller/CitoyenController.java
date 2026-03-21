package com.gestion.ApplicationSignalement.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.PhotoProbleme;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.TypeProbleme;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.ApplicationSignalement.service.CitoyenService;
import com.gestion.ApplicationSignalement.service.PhotoProblemeService;
import com.gestion.ApplicationSignalement.service.ProblemeService;
import com.gestion.ApplicationSignalement.service.TypeProblemeService;
import com.gestion.ApplicationSignalement.service.UtilisateurService;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;
import com.gestion.enums.Statut;

@RestController
@RequestMapping("/api/citoyens")
@PreAuthorize("hasRole('CITOYEN')")
public class CitoyenController {

    @Autowired
    private ProblemeService problemeService;

    @Autowired
    private CitoyenService citoyenService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private TypeProblemeService typeProblemeService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PhotoProblemeService photoProblemeService;

    // Signaler un problème
  @PostMapping("/{id}/problemes")
public ResponseEntity<?> signalerProbleme(
        @PathVariable Long id, // id du citoyen
        @RequestParam String titre,
        @RequestParam String description,
        @RequestParam String localisation,
        @RequestParam Long typeProblemeId,
        @RequestParam(required = false) List<MultipartFile> photos
) {
    try {
        Probleme probleme = new Probleme();
        probleme.setTitre(titre);
        probleme.setDescription(description);
        probleme.setQuartier(localisation);
        probleme.setDateSignalement(LocalDateTime.now());
        probleme.setStatut(Statut.SIGNALE);

        // Récupération du type de problème
        TypeProbleme type = typeProblemeService.getTypeProblemeById(typeProblemeId);
        probleme.setTypeProbleme(type);

        // Signalement par le citoyen
        Probleme nouveauProbleme = citoyenService.signalerProbleme(id, probleme, typeProblemeId);

        // Gestion des photos
        if (photos != null && !photos.isEmpty()) {
            List<PhotoProbleme> photoEntities = photoProblemeService.sauvegarderPhotos(photos, nouveauProbleme);
            if (nouveauProbleme.getPhotos() == null) {
                nouveauProbleme.setPhotos(photoEntities);
            } else {
                nouveauProbleme.getPhotos().addAll(photoEntities);
            }
            problemeService.save(nouveauProbleme);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauProbleme);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors du signalement du problème");
    }
}


    // Lister les problèmes signalés par un utilisateur
    @GetMapping("/{id}/problemes")
    public ResponseEntity<List<Probleme>> voirProblemes(@PathVariable Long id) {
        List<Probleme> problemes = citoyenService.voirProblemes(id);
        return ResponseEntity.ok(problemes);
    }

    // Modifier les informations d'un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> modifierCitoyen(@PathVariable Long id,
                                                       @RequestBody Utilisateur utilisateurModifie) {
        Utilisateur updated = utilisateurService.modifierUtilisateur(utilisateurModifie, id);
        return ResponseEntity.ok(updated);
    }

    // Modifier sa photo de profil
    @PutMapping("/utilisateurs/{id}/photo")
    public ResponseEntity<?> modifierPhotoProfil(@PathVariable Long id,
                                                 @RequestParam("photo") MultipartFile photo) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateurService.updateProfilePhoto(utilisateur, photo);

        return ResponseEntity.ok("Photo de profil mise à jour avec succès");
    }

    // Ajouter des photos à un problème existant
    @PostMapping("/problemes/{idProbleme}/photos")
    public ResponseEntity<?> ajouterPhotos(@PathVariable Long idProbleme,
                                           @RequestParam List<MultipartFile> photos,
                                           @AuthenticationPrincipal Citoyen citoyen) {
        try {
            Probleme probleme = problemeService.findByIdProbleme(idProbleme);
            if (probleme == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Problème non trouvé");
            }

            if (!probleme.getCitoyens().contains(citoyen)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous ne pouvez pas ajouter de photos à ce problème");
            }

            List<PhotoProbleme> photoEntities = photoProblemeService.sauvegarderPhotos(photos, probleme);
            probleme.getPhotos().addAll(photoEntities);
            problemeService.save(probleme);

            return ResponseEntity.ok("Photos ajoutées avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'ajout des photos");
        }
    }

}
