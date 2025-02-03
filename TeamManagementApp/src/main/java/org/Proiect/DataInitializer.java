package org.Proiect;

import jakarta.annotation.PostConstruct;
import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Dezvoltare.Curs;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer {
    @Autowired
    private DepartamentRepository departamentRepository;

    @Autowired
    private AppUserRepository utilizatorRepository;

    @Autowired
    private EchipaRepository echipaRepository;

    @Autowired
    private RepositoryProiect proiectRepository;

    @Autowired
    private CursRepository cursRepository;

    @Autowired
    private TaskRepository taskRepository;
     //@PostConstruct
    public void init() {
        System.out.println("Initializing data...");
        Departament departament1 = new Departament();
        departament1.setNumeDepartament("IT");
        departamentRepository.save(departament1);

        Departament departament2 = new Departament();
        departament2.setNumeDepartament("Marketing");
        departamentRepository.save(departament2);

        Departament departament3 = new Departament();
        departament3.setNumeDepartament("HR");
        departamentRepository.save(departament3);

        Departament departament4 = new Departament();
        departament4.setNumeDepartament("Financiar");
        departamentRepository.save(departament4);

        Departament departament5 = new Departament();
        departament5.setNumeDepartament("R&D");
        departamentRepository.save(departament5);


        Utilizator utilizator1 = new Utilizator();
        utilizator1.setNume("Ion Popescu");
        utilizator1.setEmail("ion.popescu@example.com");
        utilizator1.setTipUtilizator(TipUtilizator.MEMBRUECHIPA);
        utilizator1.setDepartament(departament1);
        utilizatorRepository.save(utilizator1);

        Utilizator utilizator2 = new Utilizator();
        utilizator2.setNume("Maria Ionescu");
        utilizator2.setEmail("maria.ionescu@example.com");
        utilizator2.setTipUtilizator(TipUtilizator.LIDER);
        utilizator2.setDepartament(departament2);
        utilizatorRepository.save(utilizator2);

        Utilizator utilizator3 = new Utilizator();
        utilizator3.setNume("Vlad Georgescu");
        utilizator3.setEmail("vlad.georgescu@example.com");
        utilizator3.setTipUtilizator(TipUtilizator.LIDER);
        utilizator3.setDepartament(departament3);
        utilizatorRepository.save(utilizator3);

        Utilizator utilizator4 = new Utilizator();
        utilizator4.setNume("Ana Vasilescu");
        utilizator4.setEmail("ana.vasilescu@example.com");
        utilizator4.setTipUtilizator(TipUtilizator.MEMBRUECHIPA);
        utilizator4.setDepartament(departament4);
        utilizatorRepository.save(utilizator4);


        Proiect proiect1 = new Proiect();
        proiect1.setDenumire("Dezvoltare Web");
        proiect1.setDescriere("Proiect pentru dezvoltarea unei aplicații web.");
        proiect1.setStatus(StatusProiect.IN_PROGRESS);
        proiect1.setDataIncepere(new Date());
        proiect1.setLider(utilizator1);
        proiectRepository.save(proiect1);

        Proiect proiect2 = new Proiect();
        proiect2.setDenumire("Campanie Publicitară");
        proiect2.setDescriere("Promovarea produsului pe piața locală.");
        proiect2.setStatus(StatusProiect.CREATED);
        proiect2.setDataIncepere(new Date());
        proiect2.setLider(utilizator2);
        proiectRepository.save(proiect2);

        Task task1 = new Task();
        task1.setDenumire("Creează arhitectura backend");
        task1.setDescriere("Arhitectura backend pentru aplicația web.");
        task1.setDataIncepere(new Date());
        task1.setDataFinalizare(new Date());
        task1.setDeadline(LocalDate.now().plusDays(30));
        task1.setStatus(Status.IN_EXECUTIE);
        task1.setProiect(proiect1);
        task1.setLider(utilizator1);
        task1.setMembru(utilizator4);
        taskRepository.save(task1);

        Echipa echipa1 = new Echipa();
        echipa1.setDenumire("Echipa Alpha");
        echipa1.setProiect(proiect1);
        echipa1.setLider(utilizator1);
        echipaRepository.save(echipa1);

        Echipa echipa2 = new Echipa();
        echipa2.setDenumire("Echipa Beta");
        echipa2.setProiect(proiect2);
        echipa2.setLider(utilizator2);
        echipaRepository.save(echipa2);
    }
    }

