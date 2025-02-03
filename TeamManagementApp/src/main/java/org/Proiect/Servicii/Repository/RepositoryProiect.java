package org.Proiect.Servicii.Repository;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.Proiect.Proiect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@RepositoryRestResource
public interface RepositoryProiect extends JpaRepository<Proiect, Integer> {

   //Găsire proiecte după status
    List<Proiect> findByStatus(StatusProiect status);

    // Găsire proiecte active (nu sunt finalizate sau anulate)
    @Query("SELECT p FROM Proiect p WHERE p.status NOT IN (:statuses)")
    List<Proiect> findActiveProjects(@Param("statuses") List<StatusProiect> statuses);

    // Găsire proiecte după lider
    List<Proiect> findByLiderUserId(Integer liderId);







}
