package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.Administrateur;
import com.gestion.ApplicationSignalement.entity.Rapport;
import com.gestion.ApplicationSignalement.repository.AdministrateurRepository;
import com.gestion.ApplicationSignalement.repository.RapportRepository;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {

    @Autowired private RapportRepository rapportRepository;
    @Autowired private AdministrateurRepository administrateurRepository;

    private static final DeviceRgb ACCENT     = new DeviceRgb(79, 142, 247);
    private static final DeviceRgb ACCENT2    = new DeviceRgb(56, 217, 169);
    private static final DeviceRgb TEXT_MAIN  = new DeviceRgb(26, 34, 54);
    private static final DeviceRgb TEXT_MUTED = new DeviceRgb(123, 143, 181);
    private static final DeviceRgb BG_LIGHT   = new DeviceRgb(240, 244, 251);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    // ── Exporter UN rapport en PDF ────────────────────────────────
    public byte[] exporterRapport(Long rapportId) throws Exception {
        Rapport rapport = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport introuvable"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer   = new PdfWriter(baos);
        PdfDocument pdf    = new PdfDocument(writer);
        Document document  = new Document(pdf, PageSize.A4);
        document.setMargins(40, 50, 40, 50);

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // ── En-tête ──
        ajouterEnTete(document, bold, regular,
                rapport.getMairie() != null ? rapport.getMairie().getNom() : "ASPU");

        // ── Titre rapport ──
        document.add(new Paragraph("RAPPORT D'INTERVENTION")
                .setFont(bold).setFontSize(16)
                .setFontColor(TEXT_MAIN)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20).setMarginBottom(6));

        document.add(new Paragraph("Référence : #" + rapportId)
                .setFont(regular).setFontSize(10)
                .setFontColor(TEXT_MUTED)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        // ── Ligne séparatrice ──
        document.add(new LineSeparator(new SolidLine(1.5f)).setMarginBottom(20));

        // ── Informations du signalement ──
        if (rapport.getProbleme() != null) {
            ajouterSection(document, bold, regular, "SIGNALEMENT CONCERNÉ");
            ajouterInfoLigne(document, bold, regular, "Titre",
                    rapport.getProbleme().getTitre());
            ajouterInfoLigne(document, bold, regular, "Type",
                    rapport.getProbleme().getTypeProbleme() != null
                            ? rapport.getProbleme().getTypeProbleme().getNomType() : "—");
            ajouterInfoLigne(document, bold, regular, "Quartier",
                    rapport.getProbleme().getQuartier());
            ajouterInfoLigne(document, bold, regular, "Statut",
                    rapport.getProbleme().getStatut().name());
            if (rapport.getProbleme().getDateSignalement() != null) {
                ajouterInfoLigne(document, bold, regular, "Date de signalement",
                        rapport.getProbleme().getDateSignalement().format(FMT));
            }
            if (rapport.getProbleme().getDateResolution() != null) {
                ajouterInfoLigne(document, bold, regular, "Date de résolution",
                        rapport.getProbleme().getDateResolution().format(FMT));
                ajouterInfoLigne(document, bold, regular, "Temps de résolution",
                        rapport.getProbleme().getTempsResolutionFormate());
            }
        }

        document.add(new Paragraph("").setMarginTop(10));

        // ── Contenu du rapport ──
        ajouterSection(document, bold, regular, "CONTENU DU RAPPORT");
        Paragraph contenu = new Paragraph(rapport.getContenu())
                .setFont(regular).setFontSize(11)
                .setFontColor(TEXT_MAIN)
                .setFixedLeading(17f)
                .setBackgroundColor(BG_LIGHT)
                .setPadding(14)
                .setMarginBottom(20);
        document.add(contenu);

        // ── Informations administrateur ──
        if (rapport.getAdministrateur() != null) {
            ajouterSection(document, bold, regular, "RÉDIGÉ PAR");
            Administrateur admin = rapport.getAdministrateur();
            ajouterInfoLigne(document, bold, regular, "Nom",
                    admin.getPrenom() + " " + admin.getNom());
            ajouterInfoLigne(document, bold, regular, "Email", admin.getEmail());
            ajouterInfoLigne(document, bold, regular, "Mairie",
                    admin.getMairie() != null ? admin.getMairie().getNom() : "—");
        }

        if (rapport.getDateRapport() != null) {
            document.add(new Paragraph("").setMarginTop(10));
            ajouterInfoLigne(document, bold, regular, "Date du rapport",
                    rapport.getDateRapport().format(FMT));
        }

        // ── Pied de page ──
        ajouterPiedDePage(document, regular);

        document.close();
        return baos.toByteArray();
    }

    // ── Exporter TOUS les rapports d'une mairie ───────────────────
    public byte[] exporterTousRapports(Long administrateurId) throws Exception {
        Administrateur admin = administrateurRepository.findById(administrateurId)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));

        List<Rapport> rapports = rapportRepository.findByMairie(admin.getMairie());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer   = new PdfWriter(baos);
        PdfDocument pdf    = new PdfDocument(writer);
        Document document  = new Document(pdf, PageSize.A4);
        document.setMargins(40, 50, 40, 50);

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // ── En-tête ──
        ajouterEnTete(document, bold, regular, admin.getMairie().getNom());

        document.add(new Paragraph("REGISTRE DES RAPPORTS D'INTERVENTION")
                .setFont(bold).setFontSize(16)
                .setFontColor(TEXT_MAIN)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20).setMarginBottom(4));

        document.add(new Paragraph("Mairie de " + admin.getMairie().getNom()
                + " — " + rapports.size() + " rapport(s)")
                .setFont(regular).setFontSize(10)
                .setFontColor(TEXT_MUTED)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        document.add(new LineSeparator(new SolidLine(1.5f)).setMarginBottom(24));

        if (rapports.isEmpty()) {
            document.add(new Paragraph("Aucun rapport disponible.")
                    .setFont(regular).setFontSize(11).setFontColor(TEXT_MUTED)
                    .setTextAlignment(TextAlignment.CENTER));
        } else {
            for (int i = 0; i < rapports.size(); i++) {
                Rapport r = rapports.get(i);

                // Titre du rapport
                document.add(new Paragraph("Rapport #" + (i + 1)
                        + (r.getProbleme() != null ? " — " + r.getProbleme().getTitre() : ""))
                        .setFont(bold).setFontSize(12)
                        .setFontColor(ACCENT)
                        .setMarginBottom(8));

                // Infos
                if (r.getProbleme() != null) {
                    document.add(creerInfoTable(bold, regular, r));
                }

                // Contenu
                document.add(new Paragraph(r.getContenu())
                        .setFont(regular).setFontSize(10)
                        .setFontColor(TEXT_MAIN)
                        .setFixedLeading(15f)
                        .setBackgroundColor(BG_LIGHT)
                        .setPadding(12)
                        .setMarginBottom(6));

                if (r.getDateRapport() != null) {
                    document.add(new Paragraph("Rédigé le " + r.getDateRapport().format(FMT))
                            .setFont(regular).setFontSize(9).setFontColor(TEXT_MUTED)
                            .setMarginBottom(20));
                }

                // Séparateur entre rapports
                if (i < rapports.size() - 1) {
                    document.add(new LineSeparator(new SolidLine(0.5f))
                            .setMarginBottom(20));
                }
            }
        }

        ajouterPiedDePage(document, regular);
        document.close();
        return baos.toByteArray();
    }

    // ── Helpers ───────────────────────────────────────────────────

    private void ajouterEnTete(Document doc, PdfFont bold, PdfFont regular, String mairie) {
        // Logo / nom
        Paragraph logo = new Paragraph("ASPU")
                .setFont(bold).setFontSize(22)
                .setFontColor(ACCENT);
        Paragraph sousTitre = new Paragraph("Application de Signalement des Problèmes Urbains")
                .setFont(regular).setFontSize(9)
                .setFontColor(TEXT_MUTED);
        Paragraph mairieP = new Paragraph(mairie)
                .setFont(bold).setFontSize(10)
                .setFontColor(TEXT_MAIN);

        // Table 2 colonnes : logo gauche, mairie droite
        Table header = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        Cell left = new Cell().add(logo).add(sousTitre)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
        Cell right = new Cell().add(mairieP)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
        header.addCell(left);
        header.addCell(right);
        doc.add(header);
    }

    private void ajouterSection(Document doc, PdfFont bold, PdfFont regular, String titre) {
        doc.add(new Paragraph(titre)
                .setFont(bold).setFontSize(9)
                .setFontColor(TEXT_MUTED)
                .setCharacterSpacing(1)
                .setMarginBottom(8)
                .setMarginTop(4));
    }

    private void ajouterInfoLigne(Document doc, PdfFont bold, PdfFont regular,
                                   String label, String valeur) {
        Table t = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(4);
        t.addCell(new Cell().add(new Paragraph(label).setFont(bold).setFontSize(10)
                .setFontColor(TEXT_MUTED))
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
        t.addCell(new Cell().add(new Paragraph(valeur != null ? valeur : "—")
                .setFont(regular).setFontSize(10).setFontColor(TEXT_MAIN))
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
        doc.add(t);
    }

    private Table creerInfoTable(PdfFont bold, PdfFont regular, Rapport r) {
        Table t = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(8)
                .setBackgroundColor(BG_LIGHT);

        String[] labels = {"Quartier", "Type", "Statut"};
        String[] vals = {
                r.getProbleme().getQuartier(),
                r.getProbleme().getTypeProbleme() != null
                        ? r.getProbleme().getTypeProbleme().getNomType() : "—",
                r.getProbleme().getStatut().name()
        };

        for (int i = 0; i < 3; i++) {
            Cell c = new Cell()
                    .add(new Paragraph(labels[i]).setFont(bold).setFontSize(8).setFontColor(TEXT_MUTED))
                    .add(new Paragraph(vals[i]).setFont(regular).setFontSize(10).setFontColor(TEXT_MAIN))
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                    .setPadding(8);
            t.addCell(c);
        }
        return t;
    }

    private void ajouterPiedDePage(Document doc, PdfFont regular) {
        doc.add(new Paragraph("").setMarginTop(30));
        doc.add(new LineSeparator(new SolidLine(0.5f))
                .setMarginBottom(8));
        doc.add(new Paragraph("© 2026 ASPU — Application de Signalement des Problèmes Urbains. Document généré automatiquement.")
                .setFont(regular).setFontSize(8)
                .setFontColor(TEXT_MUTED)
                .setTextAlignment(TextAlignment.CENTER));
    }
}