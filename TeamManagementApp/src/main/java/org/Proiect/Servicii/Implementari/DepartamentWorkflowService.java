package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.DepartamentRepository;
import org.Proiect.Servicii.IDepartamentFactory;
import org.Proiect.Servicii.IDepartamentWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartamentWorkflowService implements IDepartamentWorkflowService {
    @Autowired
    private DepartamentRepository departamentRepository;

    @Autowired
    private AppUserRepository utilizatorRepository;

    @Autowired
    private IDepartamentFactory departamentFactory; // Injectăm DepartamentFactory

    @Override
    public Departament creeazaDepartament(String numeDepartament, Integer managerId) {
        Utilizator manager = utilizatorRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Managerul nu există"));

        if (manager.getTipUtilizator() != TipUtilizator.MANAGER) {
            throw new IllegalArgumentException("Doar managerii pot crea departamente");
        }
        Departament departament = departamentFactory.creeazaDepartament(numeDepartament, manager);
        return departamentRepository.save(departament);
    }

    @Override
    public void adaugaUtilizatorInDepartament(Integer departamentId, Integer userId, String rol) {
        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new IllegalArgumentException("Departamentul nu există"));
        Utilizator utilizator = utilizatorRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu există"));
        if (departament.getAngajati() == null) {
            departament.setAngajati(new ArrayList<>());
        }
        departament.getAngajati().add(utilizator);
        utilizator.setDepartament(departament);
        utilizator.setRol(rol);

        utilizatorRepository.save(utilizator);
        departamentRepository.save(departament);
    }

    @Override
    public void modificaDepartament(Integer departamentId, String numeNou) {
        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new IllegalArgumentException("Departamentul nu există"));

        departament.setNumeDepartament(numeNou);
        departamentRepository.save(departament);
    }

    @Override
    public Departament vizualizeazaDepartament(Integer departamentId) {
        return departamentRepository.findById(departamentId)
                .orElseThrow(() -> new IllegalArgumentException("Departamentul nu există"));
    }

    @Override
    public List<Utilizator> vizualizeazaMembriiDepartament(Integer departamentId) {
        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new IllegalArgumentException("Departamentul nu există"));
        return utilizatorRepository.findByDepartamentId(departamentId);
    }
    @Override
    public List<Departament> getAllDepartamente() {
        return departamentRepository.findAll();
    }
}