package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.Dezvoltare.Curs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursRepository extends JpaRepository<Curs, Integer> {
    // Găsește cursurile care conțin un cuvânt specific în titlu
    List<Curs> findByTitluContainingIgnoreCase(String titlu);

    // Găsește cursurile care au cel puțin un badge asociat
    @Query("SELECT c FROM Curs c WHERE SIZE(c.badges) > 0")
    List<Curs> findCursuriCuBadgeuri();

    // Găsește cursurile în care un utilizator este înrolat
    @Query("SELECT c FROM Curs c JOIN UtilizatorCurs uc ON c.id = uc.curs.id WHERE uc.utilizator.id = :utilizatorId")
    List<Curs> findCursuriPentruUtilizator(Integer utilizatorId);

    // Găsește toate cursurile care nu sunt completate de un utilizator
    @Query("SELECT c FROM Curs c WHERE c.id NOT IN (SELECT uc.curs.id FROM UtilizatorCurs uc WHERE uc.utilizator.id = :utilizatorId AND uc.completat = true)")
    List<Curs> findCursuriNecompletatePentruUtilizator(Integer utilizatorId);


}
