package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Annonce;
import com.gestion.ApplicationSignalement.entity.CategorieAnnonce;
import com.gestion.ApplicationSignalement.entity.Mairie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    // Toutes les annonces d'une mairie, triées par date décroissante
    List<Annonce> findByMairieOrderByDatePublicationDesc(Mairie mairie);

    // Toutes les annonces d'une catégorie
    List<Annonce> findByCategorieOrderByDatePublicationDesc(CategorieAnnonce categorie);

    // Toutes les annonces triées par date (pour la liste globale citoyens)
    List<Annonce> findAllByOrderByDatePublicationDesc();

    // Annonces d'une mairie par catégorie
    List<Annonce> findByMairieAndCategorieOrderByDatePublicationDesc(Mairie mairie, CategorieAnnonce categorie);
}