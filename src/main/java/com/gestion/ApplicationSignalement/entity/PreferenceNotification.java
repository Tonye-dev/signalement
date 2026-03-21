package com.gestion.ApplicationSignalement.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "preferences_notifications")
public class PreferenceNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "citoyen_id", nullable = false, unique = true)
    private Citoyen citoyen;

    // Catégories pour lesquelles le citoyen veut des emails
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pref_categories_email",
                     joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private Set<CategorieAnnonce> categoriesEmail = new HashSet<>();

    // Catégories pour lesquelles le citoyen veut des SMS
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pref_categories_sms",
                     joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private Set<CategorieAnnonce> categoriesSms = new HashSet<>();

    public PreferenceNotification() {}

    public PreferenceNotification(Citoyen citoyen) {
        this.citoyen = citoyen;
    }

    public Long getId() { return id; }
    public Citoyen getCitoyen() { return citoyen; }
    public void setCitoyen(Citoyen c) { this.citoyen = c; }
    public Set<CategorieAnnonce> getCategoriesEmail() { return categoriesEmail; }
    public void setCategoriesEmail(Set<CategorieAnnonce> s) { this.categoriesEmail = s; }
    public Set<CategorieAnnonce> getCategoriesSms() { return categoriesSms; }
    public void setCategoriesSms(Set<CategorieAnnonce> s) { this.categoriesSms = s; }
}