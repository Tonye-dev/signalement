package com.gestion.ApplicationSignalement.repository;

import com.gestion.ApplicationSignalement.entity.Citoyen;
import com.gestion.ApplicationSignalement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByCitoyenOrderByDateCreationDesc(Citoyen citoyen);

    long countByCitoyenAndLueFalse(Citoyen citoyen);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.lue = true WHERE n.citoyen = :citoyen")
    void marquerToutesLues(@Param("citoyen") Citoyen citoyen);
}