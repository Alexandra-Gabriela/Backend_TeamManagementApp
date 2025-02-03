package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Raport;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.EchipaRepository;
import org.Proiect.Servicii.IEchipaFactory;
import org.Proiect.Servicii.IProiectFactory;
import org.Proiect.Servicii.IProiecteWorkflowService;

import org.Proiect.Servicii.Repository.RepositoryProiect;
import org.Proiect.Servicii.Repository.TaskRepository;
import org.Proiect.Servicii.ITaskFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProiectWorkflowService implements IProiecteWorkflowService {
    @Autowired
    private RepositoryProiect proiectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EchipaRepository echipaRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private IProiectFactory proiectFactory;
    @Autowired
    private IEchipaFactory echipaFactory;
    @Autowired
    private ITaskFactory taskFactory;

    @Override
    public Proiect creareProiect(String denumire, String descriere, Utilizator lider, List<Echipa> echipe, Date dataInceput) {
        if (!lider.getTipUtilizator().equals(TipUtilizator.LIDER)) {
            throw new IllegalArgumentException("Liderul trebuie să fie de tip LIDER.");
        }

        Proiect proiect = proiectFactory.creeazaProiectValidat(denumire, descriere, lider, echipe, dataInceput);

        if (proiect.getStatus() == null) {
            proiect.setStatus(StatusProiect.CREATED);
        }

        List<Echipa> attachedEchipe = echipe.stream()
                .map(echipa -> {
                    if (echipa.getIdEchipa() != 0) {
                        // Dacă echipa există deja în baza de date, o salvăm folosind merge
                        return echipaRepository.saveAndFlush(echipa);
                    } else {
                        // Dacă echipa este nouă, o creăm și o salvăm
                        Echipa echipaNoua = echipaFactory.creeazaEchipa(echipa.getDenumire(), lider);
                        return echipaRepository.save(echipaNoua);  // Salvăm echipa nouă
                    }
                })
                .toList();

        proiect.setEchipe(attachedEchipe);
        attachedEchipe.forEach(e -> e.setProiect(proiect));

        // Salvează proiectul
        return proiectRepository.save(proiect);
    }


    @Override
    public Proiect modificaTeamLeader(Integer proiectId, Utilizator nouLider) {
        if (!nouLider.getTipUtilizator().equals(TipUtilizator.LIDER)) {
            throw new IllegalArgumentException("Liderul trebuie să fie de tip LIDER.");
        }

        Proiect proiect = proiectRepository.findById(proiectId)
                .orElseThrow(() -> new RuntimeException("Proiectul nu a fost găsit!"));

        proiect.setLider(nouLider);
        return proiectRepository.save(proiect);
    }

    @Override
    public Proiect adaugaEchipaLaProiect(Integer proiectId, Echipa echipa) {
        Proiect proiect = proiectRepository.findById(proiectId)
                .orElseThrow(() -> new IllegalArgumentException("Proiectul nu există: " + proiectId));
        if (proiect.getEchipe() == null) {
            proiect.setEchipe(new ArrayList<>());
        } else if (!(proiect.getEchipe() instanceof ArrayList)) {
            proiect.setEchipe(new ArrayList<>(proiect.getEchipe()));
        }
        Echipa echipaNoua = echipa.getIdEchipa() == 0
                ? echipaFactory.creeazaEchipa(echipa.getDenumire(), proiect.getLider())
                : echipaRepository.findById(echipa.getIdEchipa())
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu există: " + echipa.getIdEchipa()));

        echipaNoua = echipaRepository.save(echipaNoua);
        proiect.getEchipe().add(echipaNoua);
        echipaNoua.setProiect(proiect);
        return proiectRepository.save(proiect);
    }
    @Override
    @Transactional
    public Task atribuieTaskMembru(Integer taskId, Utilizator membru) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task-ul nu există: " + taskId));
        Utilizator membruManaged = appUserRepository.findById(membru.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu există: " + membru.getUserId()));
        task.setMembru(membruManaged);
        return taskRepository.save(task);
    }

    @Override
    public Proiect actualizareStatusProiect(Integer proiectId, StatusProiect status) {
        Proiect proiect = proiectRepository.findById(proiectId)
                .orElseThrow(() -> new RuntimeException("Proiectul nu a fost găsit!"));
        proiect.setStatus(status);
        return proiectRepository.save(proiect);
    }

    @Override
    @Transactional
    public Proiect finalizareProiect(Integer proiectId) {
        Proiect proiect = proiectRepository.findById(proiectId)
                .orElseThrow(() -> new IllegalArgumentException("Proiectul nu există: " + proiectId));
        proiect.setStatus(StatusProiect.COMPLETED);
        proiect.setDataFinalizare(new Date());
        return proiectRepository.save(proiect);
    }

    @Override
    public List<Task> monitorizareTaskuri(Integer proiectId) {
        return taskRepository.findByProiectId(proiectId);
    }

    @Override
    public Raport genereazaRaport(Integer proiectId) {
        Proiect proiect = proiectRepository.findById(proiectId)
                .orElseThrow(() -> new RuntimeException("Proiectul nu a fost găsit!"));
        Raport raport = new Raport();
        raport.setProiect(proiect);
        raport.setDataGenerare(java.sql.Date.valueOf(java.time.LocalDate.now()));
        raport.setDenumire("Raport pentru proiectul: " + proiect.getDenumire());
        List<Task> taskuri = taskRepository.findByProiectId(proiectId);
        return raport;
    }

    @Override
    public boolean stergeProiect(Integer proiectId) {
        if (!proiectRepository.existsById(proiectId)) {
            throw new RuntimeException("Proiectul nu a fost găsit!");
        }
        proiectRepository.deleteById(proiectId);
        return true;
    }

    @Override
    public Proiect arhiveazaProiect(Integer proiectId) {
        Proiect proiect = proiectRepository.findById(proiectId)
                .orElseThrow(() -> new RuntimeException("Proiectul nu a fost găsit!"));
        proiect.setStatus(StatusProiect.ON_HOLD);
        return proiectRepository.save(proiect);
    }

    @Override
    public Proiect getProiectById(Integer proiectId) {
        return proiectRepository.findById(proiectId)
                .orElseThrow(() -> new RuntimeException("Proiectul nu a fost găsit!"));
    }

    @Override
    public List<Proiect> getToateProiectele() {
        return proiectRepository.findAll();
    }
}
