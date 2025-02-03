package org.Proiect.Servicii;

import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Domain.Angajati.Utilizator;

import java.util.Arrays;
import java.util.List;

public interface IDepartamentWorkflowService {

    Departament creeazaDepartament(String numeDepartament, Integer  managerId);
    void adaugaUtilizatorInDepartament(Integer departamentId, Integer  userId, String rol);
    void modificaDepartament(Integer  departamentId, String numeNou);
    Departament vizualizeazaDepartament(Integer  departamentId);
    List<Utilizator> vizualizeazaMembriiDepartament(Integer  departamentId);
    List<Departament> getAllDepartamente();

}
