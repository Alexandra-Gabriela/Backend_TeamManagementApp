package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.Angajati.*;
import org.Proiect.Domain.App.TipUtilizator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository <Utilizator, Integer> {

    // Căutare utilizatori după departament
    List<Utilizator> findByDepartamentId(Integer departamentId);

    // Căutare utilizatori după nume
    List<Utilizator> findByNumeContaining(String nume);

    // Găsire utilizator după email
    Optional<Utilizator> findByEmail(String email);

    @Query("SELECT u FROM Utilizator u WHERE u.tipUtilizator = :tipUtilizator")
    List<Utilizator> findAllByTipUtilizator(@Param("tipUtilizator") TipUtilizator tipUtilizator);


    @Query("SELECT u FROM Utilizator u JOIN Echipa e ON e.lider.id = u.id WHERE e.denumire LIKE %:denumireEchipa%")
    List<Utilizator> findLideriByEchipaDenumire(@Param("denumireEchipa") String denumireEchipa);


    // Găsire membri după echipă
    @Query("SELECT u FROM Utilizator u JOIN Echipa e ON u MEMBER OF e.membri WHERE u.tipUtilizator = 'MEMBRU_ECHIPA' AND e.idEchipa = :echipaId")
    List<Utilizator> findMembriByEchipaId(@Param("echipaId") Integer echipaId);

    // Găsire membri care au sarcini asignate
    @Query("SELECT u FROM Utilizator u WHERE u.tipUtilizator = 'MEMBRU_ECHIPA' AND EXISTS (SELECT t FROM Task t WHERE t.membru.id = u.id)")
    List<Utilizator> findMembriWithAssignedTasks();

    @Query("SELECT u FROM Utilizator u WHERE u.echipa.idEchipa = :echipaId AND u.disponibil = true AND u.tipUtilizator = :tipUtilizator AND u.id <> :exclusMembruId")
    Optional<Utilizator> findAvailableMemberExcluding(
            @Param("echipaId") Integer echipaId,
            @Param("tipUtilizator") TipUtilizator tipUtilizator,
            @Param("exclusMembruId") Integer exclusMembruId);
    @Query("SELECT u FROM Utilizator u WHERE u.echipa.idEchipa = :echipaId")
    List<Utilizator> findAllByEchipa(@Param("echipaId") Integer echipaId);

    @Query("SELECT u FROM Utilizator u JOIN UtilizatorCurs uc ON uc.utilizator.id = u.id WHERE uc.curs.id = :cursId")
    List<Utilizator> findUtilizatoriByCursId(@Param("cursId") Integer cursId);

    @Query("SELECT u FROM Utilizator u WHERE u.id NOT IN (SELECT uc.utilizator.id FROM UtilizatorCurs uc WHERE uc.curs.id = :cursId)")
    List<Utilizator> findUtilizatoriNotInCurs(@Param("cursId") Integer cursId);


}




