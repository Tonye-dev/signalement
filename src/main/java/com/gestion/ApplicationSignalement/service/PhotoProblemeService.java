package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.PhotoProbleme;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.repository.PhotoProblemeRepository;
import com.gestion.ApplicationSignalement.repository.ProblemeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoProblemeService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired // ← corrigé
    private ProblemeRepository problemeRepository;

    @Autowired // ← corrigé
    private PhotoProblemeRepository photoProblemeRepository;

    public List<PhotoProbleme> sauvegarderPhotos(List<MultipartFile> fichiers, Probleme probleme) throws IOException {
        List<PhotoProbleme> photos = new ArrayList<>();

        String dossierProbleme = uploadDir + "/probleme_" + probleme.getId();
        Path cheminDossier = Paths.get(dossierProbleme);

        if (!Files.exists(cheminDossier)) {
            Files.createDirectories(cheminDossier);
        }

        for (MultipartFile fichier : fichiers) {
            String nomOriginal = fichier.getOriginalFilename();
            String extension = nomOriginal.substring(nomOriginal.lastIndexOf("."));
            String nomUnique = UUID.randomUUID().toString() + extension;

            Path cheminFichier = cheminDossier.resolve(nomUnique);
            Files.copy(fichier.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);

            PhotoProbleme photo = new PhotoProbleme();
            photo.setNomFichier(nomUnique);
            photo.setChemin("/uploads/probleme_" + probleme.getId() + "/" + nomUnique); // ← chemin relatif pour affichage web
            photo.setProbleme(probleme);

            photos.add(photo);
        }

        return photos;
    }

    public List<PhotoProbleme> getPhotosProbleme(Long problemeId) {
        Probleme probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new RuntimeException("Problème non trouvé"));
        return photoProblemeRepository.findByProblemeOrderByDateAjoutAsc(probleme);
    }
}