package com.gestion.ApplicationSignalement.service;

import com.gestion.dto.DashboardSuperAdminDto;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PdfSuperAdminService {

    @Autowired
    private DashboardSuperAdminService dashboardService;

    private static final DeviceRgb ACCENT     = new DeviceRgb(79, 142, 247);
    private static final DeviceRgb ACCENT2    = new DeviceRgb(56, 217, 169);
    private static final DeviceRgb ACCENT3    = new DeviceRgb(247, 147, 79);
    private static final DeviceRgb ACCENT4    = new DeviceRgb(247, 111, 142);
    private static final DeviceRgb TEXT_MAIN  = new DeviceRgb(26, 34, 54);
    private static final DeviceRgb TEXT_MUTED = new DeviceRgb(123, 143, 181);
    private static final DeviceRgb BG_LIGHT   = new DeviceRgb(240, 244, 251);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    // ── Rapport global ────────────────────────────────────────────
    public byte[] genererRapportGlobal() throws Exception {
        DashboardSuperAdminDto d = dashboardService.getDashboard();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter   writer   = new PdfWriter(baos);
        PdfDocument pdf      = new PdfDocument(writer);
        Document    document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 50, 40, 50);

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        ajouterEnTete(document, bold, regular, "Rapport Global de la Plateforme ASPU");

        // ── Totaux ──
        ajouterSection(document, bold, "STATISTIQUES GLOBALES");
        Table t = new Table(UnitValue.createPercentArray(new float[]{1,1,1,1}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
        ajouterStatCell(t, bold, regular, "Mairies",        String.valueOf(d.getTotalMairies()),       ACCENT);
        ajouterStatCell(t, bold, regular, "Utilisateurs",   String.valueOf(d.getTotalUtilisateurs()),  ACCENT2);
        ajouterStatCell(t, bold, regular, "Signalements",   String.valueOf(d.getTotalProblemes()),     ACCENT3);
        ajouterStatCell(t, bold, regular, "Résolus",        String.valueOf(d.getProblemesResolus()),   ACCENT2);
        document.add(t);

        // ── Signalements par mairie ──
        if (d.getProblemesParMairie() != null && !d.getProblemesParMairie().isEmpty()) {
            ajouterSection(document, bold, "SIGNALEMENTS PAR MAIRIE");
            ajouterTableau2Col(document, bold, regular, d.getProblemesParMairie(), "Mairie", "Signalements");
        }

        // ── Résolus par mairie ──
        if (d.getResolusParMairie() != null && !d.getResolusParMairie().isEmpty()) {
            ajouterSection(document, bold, "RÉSOLUTIONS PAR MAIRIE");
            ajouterTableau2Col(document, bold, regular, d.getResolusParMairie(), "Mairie", "Résolus");
        }

        // ── Types fréquents ──
        if (d.getRepartitionParTypeProbleme() != null && !d.getRepartitionParTypeProbleme().isEmpty()) {
            ajouterSection(document, bold, "TYPES DE PROBLÈMES LES PLUS FRÉQUENTS");
            ajouterTableau2Col(document, bold, regular, d.getRepartitionParTypeProbleme(), "Type", "Occurrences");
        }

        // ── Temps moyen par type ──
        if (d.getTempsMoyenParTypeGlobal() != null && !d.getTempsMoyenParTypeGlobal().isEmpty()) {
            ajouterSection(document, bold, "TEMPS MOYEN DE RÉSOLUTION PAR TYPE");
            Table tt = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                    .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
            tt.addHeaderCell(cellHeader(bold, "Type de problème"));
            tt.addHeaderCell(cellHeader(bold, "Temps moyen"));
            d.getTempsMoyenParTypeGlobal().forEach((type, heures) -> {
                tt.addCell(cellData(regular, type));
                tt.addCell(cellData(regular, DashboardSuperAdminDto.formaterHeures(heures)));
            });
            document.add(tt);
        }

        ajouterPiedDePage(document, regular);
        document.close();
        return baos.toByteArray();
    }

    // ── Rapport classements ───────────────────────────────────────
    public byte[] genererRapportClassements() throws Exception {
        DashboardSuperAdminDto d = dashboardService.getDashboard();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter   writer   = new PdfWriter(baos);
        PdfDocument pdf      = new PdfDocument(writer);
        Document    document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 50, 40, 50);

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        ajouterEnTete(document, bold, regular, "Classements Officiels des Mairies");

        // ── Classement par résolutions ──
        ajouterSection(document, bold, "🏆 CLASSEMENT PAR NOMBRE DE RÉSOLUTIONS");
        if (d.getClassementResolutions() != null && !d.getClassementResolutions().isEmpty()) {
            Table t = new Table(UnitValue.createPercentArray(new float[]{0.5f, 2f, 1f}))
                    .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
            t.addHeaderCell(cellHeader(bold, "Rang"));
            t.addHeaderCell(cellHeader(bold, "Mairie"));
            t.addHeaderCell(cellHeader(bold, "Problèmes résolus"));
            AtomicInteger rang = new AtomicInteger(1);
            d.getClassementResolutions().forEach((mairie, nb) -> {
                int r = rang.getAndIncrement();
                String medal = r == 1 ? "🥇 1er" : r == 2 ? "🥈 2e" : r == 3 ? "🥉 3e" : r + "e";
                t.addCell(cellData(bold, medal));
                t.addCell(cellData(regular, mairie));
                t.addCell(cellData(bold, String.valueOf(nb)));
            });
            document.add(t);
        } else {
            document.add(new Paragraph("Aucune donnée disponible.").setFont(regular).setFontSize(10).setFontColor(TEXT_MUTED).setMarginBottom(16));
        }

        // ── Classement par rapidité ──
        ajouterSection(document, bold, "⚡ CLASSEMENT PAR RAPIDITÉ DE RÉSOLUTION");
        if (d.getClassementRapidite() != null && !d.getClassementRapidite().isEmpty()) {
            Table t = new Table(UnitValue.createPercentArray(new float[]{0.5f, 2f, 1f}))
                    .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
            t.addHeaderCell(cellHeader(bold, "Rang"));
            t.addHeaderCell(cellHeader(bold, "Mairie"));
            t.addHeaderCell(cellHeader(bold, "Temps moyen"));
            AtomicInteger rang = new AtomicInteger(1);
            d.getClassementRapidite().forEach((mairie, heures) -> {
                int r = rang.getAndIncrement();
                String medal = r == 1 ? "🥇 1er" : r == 2 ? "🥈 2e" : r == 3 ? "🥉 3e" : r + "e";
                t.addCell(cellData(bold, medal));
                t.addCell(cellData(regular, mairie));
                t.addCell(cellData(bold, DashboardSuperAdminDto.formaterHeures(heures)));
            });
            document.add(t);
        } else {
            document.add(new Paragraph("Aucune donnée disponible.").setFont(regular).setFontSize(10).setFontColor(TEXT_MUTED).setMarginBottom(16));
        }

        // ── Résolutions par mairie par mois ──
        if (d.getResolutionsParMairieParMois() != null && !d.getResolutionsParMairieParMois().isEmpty()) {
            ajouterSection(document, bold, "📅 RÉSOLUTIONS PAR MAIRIE PAR MOIS");
            d.getResolutionsParMairieParMois().forEach((mois, mairies) -> {
                document.add(new Paragraph(mois)
                        .setFont(bold).setFontSize(10).setFontColor(ACCENT).setMarginBottom(6));
                Table t = new Table(UnitValue.createPercentArray(new float[]{2f, 1f}))
                        .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(10);
                t.addHeaderCell(cellHeader(bold, "Mairie"));
                t.addHeaderCell(cellHeader(bold, "Résolus"));
                mairies.forEach((mairie, nb) -> {
                    t.addCell(cellData(regular, mairie));
                    t.addCell(cellData(bold, String.valueOf(nb)));
                });
                document.add(t);
            });
        }

        // ── Types par quartier ──
        if (d.getTypesParQuartierGlobal() != null && !d.getTypesParQuartierGlobal().isEmpty()) {
            ajouterSection(document, bold, "📍 TYPES DE PROBLÈMES PAR QUARTIER");
            Table t = new Table(UnitValue.createPercentArray(new float[]{1.5f, 1.5f, 0.8f}))
                    .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
            t.addHeaderCell(cellHeader(bold, "Quartier"));
            t.addHeaderCell(cellHeader(bold, "Type de problème"));
            t.addHeaderCell(cellHeader(bold, "Occurrences"));
            d.getTypesParQuartierGlobal().forEach((quartier, types) ->
                types.forEach((type, nb) -> {
                    t.addCell(cellData(regular, quartier));
                    t.addCell(cellData(regular, type));
                    t.addCell(cellData(bold, String.valueOf(nb)));
                })
            );
            document.add(t);
        }

        ajouterPiedDePage(document, regular);
        document.close();
        return baos.toByteArray();
    }

    // ── Helpers ───────────────────────────────────────────────────

    private void ajouterEnTete(Document doc, PdfFont bold, PdfFont regular, String titre) {
        Table header = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        header.addCell(new Cell()
                .add(new Paragraph("ASPU").setFont(bold).setFontSize(22).setFontColor(ACCENT))
                .add(new Paragraph("Application de Signalement des Problèmes Urbains")
                        .setFont(regular).setFontSize(9).setFontColor(TEXT_MUTED))
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
        header.addCell(new Cell()
                .add(new Paragraph("Généré le " + LocalDateTime.now().format(FMT))
                        .setFont(regular).setFontSize(9).setFontColor(TEXT_MUTED))
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT));
        doc.add(header);

        doc.add(new Paragraph(titre)
                .setFont(bold).setFontSize(16).setFontColor(TEXT_MAIN)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(16).setMarginBottom(6));

        doc.add(new LineSeparator(new SolidLine(1.5f)).setMarginBottom(20));
    }

    private void ajouterSection(Document doc, PdfFont bold, String titre) {
        doc.add(new Paragraph(titre)
                .setFont(bold).setFontSize(9).setFontColor(TEXT_MUTED)
                .setCharacterSpacing(1).setMarginTop(14).setMarginBottom(8));
    }

    private void ajouterStatCell(Table t, PdfFont bold, PdfFont regular,
                                  String label, String val, DeviceRgb color) {
        Cell c = new Cell()
                .add(new Paragraph(val).setFont(bold).setFontSize(22).setFontColor(color))
                .add(new Paragraph(label).setFont(regular).setFontSize(9).setFontColor(TEXT_MUTED))
                .setBackgroundColor(BG_LIGHT).setPadding(14)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
        t.addCell(c);
    }

    private void ajouterTableau2Col(Document doc, PdfFont bold, PdfFont regular,
                                     Map<String, Long> data, String col1, String col2) {
        Table t = new Table(UnitValue.createPercentArray(new float[]{2f, 1f}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
        t.addHeaderCell(cellHeader(bold, col1));
        t.addHeaderCell(cellHeader(bold, col2));
        data.forEach((k, v) -> {
            t.addCell(cellData(regular, k));
            t.addCell(cellData(bold, String.valueOf(v)));
        });
        doc.add(t);
    }

    private Cell cellHeader(PdfFont bold, String text) {
        return new Cell()
                .add(new Paragraph(text).setFont(bold).setFontSize(9).setFontColor(TEXT_MUTED))
                .setBackgroundColor(BG_LIGHT)
                .setPadding(8);
    }

    private Cell cellData(PdfFont font, String text) {
        return new Cell()
                .add(new Paragraph(text != null ? text : "—").setFont(font).setFontSize(10).setFontColor(TEXT_MAIN))
                .setPadding(7);
    }

    private void ajouterPiedDePage(Document doc, PdfFont regular) {
        doc.add(new Paragraph("").setMarginTop(24));
        doc.add(new LineSeparator(new SolidLine(0.5f)).setMarginBottom(8));
        doc.add(new Paragraph("© 2026 ASPU — Document officiel généré automatiquement.")
                .setFont(regular).setFontSize(8).setFontColor(TEXT_MUTED)
                .setTextAlignment(TextAlignment.CENTER));
    }
}