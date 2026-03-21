package com.gestion.ApplicationSignalement.controller;

import com.gestion.ApplicationSignalement.service.PdfSuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf/superadmin")
@PreAuthorize("hasRole('SUPERADMIN')")
public class PdfSuperAdminController {

    @Autowired private PdfSuperAdminService pdfSuperAdminService;

    // ── Rapport global ────────────────────────────────────────────
    @GetMapping("/rapport-global")
    public ResponseEntity<byte[]> rapportGlobal() throws Exception {
        byte[] pdf = pdfSuperAdminService.genererRapportGlobal();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"rapport-global-aspu.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ── Classements ───────────────────────────────────────────────
    @GetMapping("/classements")
    public ResponseEntity<byte[]> classements() throws Exception {
        byte[] pdf = pdfSuperAdminService.genererRapportClassements();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"classements-mairies.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}