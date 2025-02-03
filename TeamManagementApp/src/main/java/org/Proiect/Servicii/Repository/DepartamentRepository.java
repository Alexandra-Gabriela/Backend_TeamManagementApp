package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.Angajati.Departament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentRepository extends JpaRepository<Departament, Integer> {
    // Găsire departament după nume
    Optional<Departament> findByNumeDepartament(String numeDepartament);

    // Găsire departamente pentru un manager de proiect
    List<Departament> findByManagerProiect_UserId(Integer managerId);

    // Găsire departamente după manager și nume parțial
    List<Departament> findByNumeDepartamentContainingAndManagerProiect_UserId(String nume, Integer managerId);
}
