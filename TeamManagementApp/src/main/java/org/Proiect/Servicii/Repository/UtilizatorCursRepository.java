package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.Dezvoltare.UtilizatorCurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilizatorCursRepository extends JpaRepository<UtilizatorCurs, Integer> {

    // Găsește toate cursurile în care un utilizator este înrolat
    List<UtilizatorCurs> findByUtilizator_UserId(Integer userId);

    // Găsește un anumit curs pentru un utilizator specific
    UtilizatorCurs findByUtilizator_UserIdAndCurs_Id(Integer userId, Integer cursId);

    // Găsește toate cursurile completate de un utilizator
    @Query("SELECT uc FROM UtilizatorCurs uc WHERE uc.utilizator.userId = :utilizatorId AND uc.completat = true")
    List<UtilizatorCurs> findCursuriCompletateDeUtilizator(@Param("utilizatorId") Integer userId);

    // Găsește toate cursurile necompletate de un utilizator
    @Query("SELECT uc FROM UtilizatorCurs uc WHERE uc.utilizator.userId = :utilizatorId AND uc.completat = false")
    List<UtilizatorCurs> findCursuriNecompletateDeUtilizator(@Param("utilizatorId") Integer userId);

    // Găsește progresul unui utilizator într-un curs
    @Query("SELECT uc.progres FROM UtilizatorCurs uc WHERE uc.utilizator.userId = :utilizatorId AND uc.curs.id = :cursId")
    Integer findProgresPentruCurs(@Param("utilizatorId") Integer userId, @Param("cursId") Integer cursId);

    // Găsește utilizatorii înscriși într-un curs
    @Query("SELECT u FROM Utilizator u JOIN UtilizatorCurs uc ON uc.utilizator.userId = u.userId WHERE uc.curs.id = :cursId")
    List<Utilizator> findUtilizatoriByCursId(@Param("cursId") Integer cursId);

    // Găsește utilizatorii care nu sunt înscriși într-un anumit curs
    @Query("SELECT u FROM Utilizator u WHERE u.userId NOT IN (SELECT uc.utilizator.userId FROM UtilizatorCurs uc WHERE uc.curs.id = :cursId)")
    List<Utilizator> findUtilizatoriNotInCurs(@Param("cursId") Integer cursId);

    // Găsește toate înregistrările utilizatorilor într-un curs
    @Query("SELECT uc FROM UtilizatorCurs uc WHERE uc.curs.id = :cursId")
    List<UtilizatorCurs> findByCurs_Id(@Param("cursId") Integer cursId);
}
