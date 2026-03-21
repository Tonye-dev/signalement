package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.CategorieAnnonce;
import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.PreferenceNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceNotificationRepository extends JpaRepository<PreferenceNotification, Long> {

    Optional<PreferenceNotification> findByCitoyen(Citoyen citoyen);

    @Query("SELECT p FROM PreferenceNotification p JOIN p.categoriesEmail c WHERE c = :cat")
    List<PreferenceNotification> findAbonnesEmail(@Param("cat") CategorieAnnonce cat);

    @Query("SELECT p FROM PreferenceNotification p JOIN p.categoriesSms c WHERE c = :cat")
    List<PreferenceNotification> findAbonnesSms(@Param("cat") CategorieAnnonce cat);
}