package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.Dezvoltare.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository  extends JpaRepository<Badge, Integer> {

    // Găsește badge-uri pentru un anumit curs
    List<Badge> findByCurs_Id(Integer cursId);

    // Găsește badge-uri în funcție de titlu
    List<Badge> findByTitluContainingIgnoreCase(String titlu);

    // Găsește toate badge-urile asociate utilizatorilor unui curs specific
    @Query("SELECT b FROM Badge b JOIN b.curs c JOIN UtilizatorCurs uc ON c.id = uc.curs.id WHERE uc.utilizator.id = :utilizatorId")
    List<Badge> findBadgesForUserAndCurs(Integer utilizatorId);

    // Găsește badge-urile de o dificultate specifică
    List<Badge> findByDificultate(Integer dificultate);
}
