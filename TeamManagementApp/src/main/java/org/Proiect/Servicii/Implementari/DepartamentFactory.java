package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Servicii.IDepartamentFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DepartamentFactory implements IDepartamentFactory {
    @Override
    public Departament creeazaDepartament(String numeDepartament, Utilizator manager) {
        if (numeDepartament == null || numeDepartament.isBlank()) {
            throw new IllegalArgumentException("Numele departamentului este obligatoriu.");
        }
        if (manager == null) {
            throw new IllegalArgumentException("Managerul departamentului nu poate fi null.");
        }
        Departament departament = new Departament();
        departament.setNumeDepartament(numeDepartament);
        departament.setManagerProiect(manager);
        departament.setAngajati(new ArrayList<>());
        return departament;
    }
    }

