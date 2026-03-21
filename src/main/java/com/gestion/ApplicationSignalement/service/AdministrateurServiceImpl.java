package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.*;
import com.gestion.ApplicationSignalement.repository.*;
import com.gestion.enums.Statut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {

    @Autowired private AdministrateurRepository administrateurRepository;
    @Autowired private ProblemeRepository problemeRepository;
    @Autowired private RapportRepository rapportRepository;
    @Autowired private PhotoProblemeRepository photoProblemeRepository;
    @Autowired private PhotoResolutionRepository photoResolutionRepository;
    @Autowired private EmailService emailService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ── Voir les problèmes assignés ───────────────────────────────
    @Override
    public List<Probleme> voirProblemesAssignes(Long administrateurId) {
        Administrateur admin = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));
        return problemeRepository.findByMairie(admin.getMairie());
    }

    // ── Filtrer les problèmes ─────────────────────────────────────
    @Override
    public List<Probleme> filtrerProblemes(Long administrateurId, String statutStr,
                                           LocalDate dateDebut, LocalDate dateFin) {
        Administrateur admin = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        Statut statut = null;
        if (statutStr != null && !statutStr.isEmpty()) {
            statut = Statut.valueOf(statutStr.toUpperCase());
        }

        if (statut != null && dateDebut != null && dateFin != null) {
            return problemeRepository.findByMairieAndStatutAndDateSignalementBetween(
                    admin.getMairie(), statut, dateDebut, dateFin);
        } else if (statut != null) {
            return problemeRepository.findByMairieAndStatut(admin.getMairie(), statut);
        } else if (dateDebut != null && dateFin != null) {
            return problemeRepository.findByMairieAndDateSignalementBetween(
                    admin.getMairie(), dateDebut, dateFin);
        } else {
            return problemeRepository.findByMairie(admin.getMairie());
        }
    }

    // ── Changer le statut + enregistrer dateResolution ────────────
    @Override
    public Probleme changerStatutProbleme(Long problemeId, String nouveauStatutStr) {
        Probleme probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new RuntimeException("Problème introuvable"));

        Statut ancienStatut = probleme.getStatut();
        Statut nouveauStatut = Statut.valueOf(nouveauStatutStr.toUpperCase());

        probleme.setStatut(nouveauStatut);

        // ← Enregistrement automatique de la date de résolution
        if (nouveauStatut == Statut.RESOLU && probleme.getDateResolution() == null) {
            probleme.setDateResolution(LocalDateTime.now());
        }
        // Si on repasse en SIGNALE ou EN_COURS, on efface la date de résolution
        if (nouveauStatut != Statut.RESOLU) {
            probleme.setDateResolution(null);
        }

        Probleme saved = problemeRepository.save(probleme);
        emailService.envoyerEmailNotification(saved, ancienStatut);
        return saved;
    }

    // ── Ajouter photos de résolution ──────────────────────────────
    @Override
    public List<PhotoResolution> ajouterPhotosResolution(Long problemeId,
                                                          Long administrateurId,
                                                          List<MultipartFile> photos) throws IOException {
        Probleme probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new RuntimeException("Problème introuvable"));
        Administrateur admin = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        String dossier = uploadDir + "/resolution_" + problemeId;
        Path cheminDossier = Paths.get(dossier);
        if (!Files.exists(cheminDossier)) Files.createDirectories(cheminDossier);

        List<PhotoResolution> result = photos.stream().map(fichier -> {
            try {
                String nom = UUID.randomUUID() + fichier.getOriginalFilename()
                        .substring(fichier.getOriginalFilename().lastIndexOf("."));
                Path dest = cheminDossier.resolve(nom);
                Files.copy(fichier.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

                PhotoResolution photo = new PhotoResolution(
                        nom,
                        "/uploads/resolution_" + problemeId + "/" + nom,
                        probleme,
                        admin
                );
                return photoResolutionRepository.save(photo);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la sauvegarde de la photo", e);
            }
        }).collect(Collectors.toList());

        return result;
    }

    // ── Rapport ───────────────────────────────────────────────────
    @Override
    public Rapport ajouterRapport(Long problemeId, Long administrateurId, String contenu) {
        Probleme probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new RuntimeException("Problème introuvable"));
        Administrateur admin = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        Rapport rapport = new Rapport(contenu, probleme, admin,
                                      admin.getMairie(), LocalDateTime.now());
        return rapportRepository.save(rapport);
    }

    @Override
    public List<Rapport> voirRapportsMairie(Long administrateurId) {
        Administrateur admin = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));
        return rapportRepository.findByMairie(admin.getMairie());
    }

    @Override
    public List<String> voirPhotosProbleme(Long problemeId) {
        Probleme probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new RuntimeException("Problème introuvable"));
        return photoProblemeRepository.findByProblemeOrderByDateAjoutAsc(probleme)
                .stream().map(PhotoProbleme::getChemin).toList();
    }

    @Override
    public boolean existeAdmin(String email) {
        return administrateurRepository.findByEmail(email).isPresent();
    }
}