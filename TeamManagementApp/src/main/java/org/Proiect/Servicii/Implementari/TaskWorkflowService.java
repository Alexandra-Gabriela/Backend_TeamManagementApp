package org.Proiect.Servicii.Implementari;


import org.Proiect.DTO.TaskDTO;
import org.Proiect.DTO.UtilizatorDTO;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.TaskRepository;
import org.Proiect.Servicii.ITaskFactory;
import org.Proiect.Servicii.ITaskWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskWorkflowService implements ITaskWorkflowService {
    private static final Logger logger = LoggerFactory.getLogger(TaskWorkflowService.class);
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ITaskFactory taskFactory;

    @Override
    public Integer creeazaTaskNou(String descriereTask, Date dataLimita) {
        Task task = taskFactory.creeazaTaskValidat(descriereTask, Status.NOU, dataLimita, null);
        Task savedTask = taskRepository.save(task);
        return savedTask.getTaskUserId();
    }

    @Override
    public void asignareTaskMembru(Integer taskId, Integer membruId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<Utilizator> membruOptional = appUserRepository.findById(membruId);
        if (taskOptional.isPresent() && membruOptional.isPresent()) {
            Task task = taskOptional.get();
            taskFactory.asignareTask(task, membruOptional.get());
            taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task sau membru nu a fost găsit!");
        }
    }

    @Override
    public void modificareTask(Integer taskId, String descriereNoua) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDescriere(descriereNoua);
            taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task-ul nu a fost găsit!");
        }
    }

    @Override
    public void stergereTask(Integer taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new IllegalArgumentException("Task-ul nu a fost găsit!");
        }
    }

    @Override
    public String vizualizareTask(Integer taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            return task.toString();
        } else {
            throw new IllegalArgumentException("Task-ul nu a fost găsit!");
        }
    }

    @Override
    public void schimbareStatusTask(Integer taskId, String statusNou) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Status status = Status.valueOf(statusNou.toUpperCase());
            task.setStatus(status);
            taskRepository.save(task);

            if (status == Status.FINALIZAT) {
                trimiteNotificareLider(taskId);
            }
        } else {
            throw new IllegalArgumentException("Task-ul nu a fost găsit!");
        }
    }

    @Override
    public void trimiteNotificareLider(Integer taskId) {
        System.out.println("Notificare trimisă liderului pentru task-ul: " + taskId);
    }


    @Override
    public void gestioneazaDepasireDeadline(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task-ul nu există: " + taskId));

        if (task.getMembru() == null || task.getMembru().getEchipa() == null) {
            throw new IllegalArgumentException("Task-ul nu are un membru sau echipa asociată!");
        }

        if (task.getMembru().getDepasiriDeadline() > 5) {
            Integer echipaId = task.getMembru().getEchipa().getIdEchipa();
            Optional<Utilizator> altMembru = appUserRepository.findAvailableMemberExcluding(
                    echipaId, TipUtilizator.MEMBRUECHIPA, task.getMembru().getUserId());

            if (altMembru.isPresent()) {
                task.setMembru(altMembru.get());
                taskRepository.save(task);
            } else {
                logger.warn("Nu s-a găsit alt membru disponibil pentru task-ul {}", taskId);
                trimiteNotificareLider(taskId);
            }
        }
    }

    @Override
    public void inrolareMembruLaCurs(Integer membruId) {
        System.out.println("Membrul cu ID-ul " + membruId + " a fost înscris la curs.");
    }

    @Override
    public void finalizareCursSiBadge(Integer membruId) {
        System.out.println("Membrul cu ID-ul " + membruId + " a finalizat cursul și a primit badge.");
    }

    @Override
    public void valorificaTask(Integer taskId, Integer membruId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Date currentDate = new Date();
            if (task.getDataFinalizare().after(currentDate)) {
                System.out.println("Task-ul a fost valorificat pentru membrul cu ID-ul " + membruId);
            } else if (task.getDataFinalizare().equals(currentDate)) {
                System.out.println("Task-ul a fost finalizat la deadline, fără puncte valorificate.");
            }
        } else {
            throw new IllegalArgumentException("Task-ul nu a fost găsit!");
        }
    }

    @Override
    public void reatribuireTask(Integer taskId, Integer membruIdNou) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Optional<Utilizator> nouMembruOptional = appUserRepository.findById(membruIdNou);

            if (nouMembruOptional.isPresent()) {
                task.setMembru(nouMembruOptional.get());
                taskRepository.save(task);
            } else {
                throw new IllegalArgumentException("Noul membru nu a fost găsit!");
            }
        } else {
            throw new IllegalArgumentException("Task-ul nu a fost găsit!");
        }
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskDTO(
                        task.getTaskUserId(),
                        task.getDenumire(),
                        task.getDescriere(),
                        task.getStatus(),
                        task.getDeadline(),
                        mapToDTO(task.getMembru())
                ))
                .collect(Collectors.toList());
    }
    private UtilizatorDTO mapToDTO(Utilizator utilizator) {
        if (utilizator == null) {
            return null;
        }
        return new UtilizatorDTO();
    }


    private Integer gasesteAltMembru(Integer proiectId, Integer exclusMembruId) {
        List<Utilizator> membri = appUserRepository.findMembriByEchipaId(proiectId);

        System.out.println("Membri inițiali: " + membri);

        List<Utilizator> membriDisponibili = membri.stream()
                .filter(m -> !m.getUserId().equals(exclusMembruId)) // Evităm membrul exclus
                .filter(Utilizator::isDisponibil) // Doar membrii disponibili
                .collect(Collectors.toList());

        System.out.println("Membri disponibili: " + membriDisponibili);

        return membriDisponibili.stream()
                .findFirst()
                .map(Utilizator::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("Nu s-a găsit alt membru disponibil!"));
    }
}