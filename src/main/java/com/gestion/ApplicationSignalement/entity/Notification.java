package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private boolean lue = false;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "citoyen_id", nullable = false)
    private Citoyen citoyen;

    @ManyToOne
    @JoinColumn(name = "annonce_id")
    private Annonce annonce; // origine de la notification

    public Notification() {}

    public Notification(String titre, String message, Citoyen citoyen, Annonce annonce) {
        this.titre        = titre;
        this.message      = message;
        this.citoyen      = citoyen;
        this.annonce      = annonce;
        this.dateCreation = LocalDateTime.now();
        this.lue          = false;
    }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public void setTitre(String t) { this.titre = t; }
    public String getMessage() { return message; }
    public void setMessage(String m) { this.message = m; }
    public boolean isLue() { return lue; }
    public void setLue(boolean l) { this.lue = l; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime d) { this.dateCreation = d; }
    public Citoyen getCitoyen() { return citoyen; }
    public void setCitoyen(Citoyen c) { this.citoyen = c; }
    public Annonce getAnnonce() { return annonce; }
    public void setAnnonce(Annonce a) { this.annonce = a; }
}