package org.Proiect.Servicii;

import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Domain.Angajati.Utilizator;

public interface IDepartamentFactory {
    /**
     * Creează un nou departament.
     *
     * @param numeDepartament Numele departamentului.
     * @param manager Managerul departamentului.
     * @return Un obiect de tip Departament inițializat.
     */
    Departament creeazaDepartament(String numeDepartament, Utilizator manager);
}
