package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.entity.PhotoResolution;
import com.gestion.ApplicationSignalement.entity.Probleme;
import com.gestion.ApplicationSignalement.entity.Rapport;
import com.gestion.ApplicationSignalement.service.AdministrateurService;
import com.gestion.ApplicationSignalement.service.PhotoProblemeService;
import com.gestion.dto.PhotoProblemeDto;
import com.gestion.dto.ProblemeDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/administrateurs")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class AdministrateurController {

    @Autowired private AdministrateurService administrateurService;
    @Autowired private PhotoProblemeService photoProblemeService;

    @GetMapping("/{id}/problemes")
    public ResponseEntity<List<ProblemeDto>> voirProblemesAssignes(@PathVariable Long id) {
        List<Probleme> problemes = administrateurService.voirProblemesAssignes(id);
        List<ProblemeDto> dto = problemes.stream().map(p -> {
            var photosDto = photoProblemeService.getPhotosProbleme(p.getId()).stream()
                    .map(ph -> new PhotoProblemeDto(ph.getChemin(), ph.getDateAjout()))
                    .collect(Collectors.toList());
            return new ProblemeDto(p.getTitre(), p.getDescription(),
                    p.getTypeProbleme(), p.getQuartier(), p.getStatut(), photosDto);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/problemes/filtrer")
    public ResponseEntity<List<Probleme>> filtrerProblemes(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) LocalDate dateDebut,
            @RequestParam(required = false) LocalDate dateFin) {
        return ResponseEntity.ok(
                administrateurService.filtrerProblemes(id, statut, dateDebut, dateFin));
    }

    @PatchMapping("/problemes/{problemeId}/statut")
    public ResponseEntity<Probleme> changerStatut(
            @PathVariable Long problemeId,
            @RequestParam String nouveauStatut) {
        return ResponseEntity.ok(
                administrateurService.changerStatutProbleme(problemeId, nouveauStatut));
    }

    @PostMapping("/problemes/{problemeId}/photos-resolution")
    public ResponseEntity<List<PhotoResolution>> ajouterPhotosResolution(
            @PathVariable Long problemeId,
            @RequestParam Long administrateurId,
            @RequestParam List<MultipartFile> photos) throws Exception {
        return ResponseEntity.ok(
                administrateurService.ajouterPhotosResolution(problemeId, administrateurId, photos));
    }

    @PostMapping("/problemes/{problemeId}/rapports")
    public ResponseEntity<Rapport> ajouterRapport(
            @PathVariable Long problemeId,
            @RequestParam Long administrateurId,
            @RequestParam String contenu) {
        return ResponseEntity.ok(
                administrateurService.ajouterRapport(problemeId, administrateurId, contenu));
    }

    @GetMapping("/{id}/rapports")
    public ResponseEntity<List<Rapport>> voirRapports(@PathVariable Long id) {
        return ResponseEntity.ok(administrateurService.voirRapportsMairie(id));
    }

    @GetMapping("/problemes/{problemeId}/photos")
    public ResponseEntity<List<String>> voirPhotos(@PathVariable Long problemeId) {
        return ResponseEntity.ok(administrateurService.voirPhotosProbleme(problemeId));
    }
}