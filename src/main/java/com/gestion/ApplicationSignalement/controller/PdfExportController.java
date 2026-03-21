package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class PdfExportController {

    @Autowired private PdfExportService pdfExportService;

    // ── Télécharger UN rapport en PDF ─────────────────────────────
    @GetMapping("/rapports/{rapportId}")
    public ResponseEntity<byte[]> exporterRapport(@PathVariable Long rapportId) throws Exception {
        byte[] pdf = pdfExportService.exporterRapport(rapportId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"rapport-" + rapportId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ── Télécharger TOUS les rapports d'une mairie en PDF ─────────
    @GetMapping("/rapports/mairie/{administrateurId}")
    public ResponseEntity<byte[]> exporterTousRapports(
            @PathVariable Long administrateurId) throws Exception {
        byte[] pdf = pdfExportService.exporterTousRapports(administrateurId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"registre-rapports.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}