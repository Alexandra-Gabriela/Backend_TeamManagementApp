package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EchipaRepository extends JpaRepository<Echipa,Integer> {
    // Găsire echipe după lider
    List<Echipa> findByLiderUserId(Integer liderId);

    // Găsire echipe cu un anumit număr de membri
    @Query("SELECT e FROM Echipa e JOIN e.membri m GROUP BY e HAVING COUNT(m) = :numarMembri")
    List<Echipa> findByNumarMembri(@Param("numarMembri") int numarMembri);

    // Găsire echipe după denumire
    List<Echipa> findByDenumireContaining(String denumire);
    @Query("SELECT u FROM Utilizator u WHERE u.echipa.idEchipa = :echipaId AND u.disponibil = true AND u.tipUtilizator = :tipUtilizator")
    List<Utilizator> findAllByEchipaIdAndDisponibilTrueAndTipUtilizator(
            @Param("echipaId") Integer echipaId,
            @Param("tipUtilizator") TipUtilizator tipUtilizator);
    @Modifying
    @Query("UPDATE Echipa e SET e.arhivata = :arhivata WHERE e.idEchipa = :echipaId")
    void updateArhivare(@Param("echipaId") Integer echipaId, @Param("arhivata") boolean arhivata);
}
