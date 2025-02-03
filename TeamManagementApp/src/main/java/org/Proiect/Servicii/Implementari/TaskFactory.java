package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.ITaskFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskFactory implements ITaskFactory {

    @Override
    public Task creeazaTaskValidat(String descriere, Status status, Date dataFinalizare, Utilizator utilizator) {
        Task task = new Task();
        task.setDescriere(descriere);
        task.setStatus(status);
        task.setDataFinalizare(dataFinalizare);
        task.setMembru(utilizator);
        return task;
    }
    @Override
    public Task creeazaTaskDinSablon(String sablon, Utilizator membru) {
        Task task = new Task();
        task.setDescriere("Task generat din șablon: " + sablon);
        task.setDataFinalizare(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        task.setMembru(membru);
        return task;
    }

    @Override
    public void asignareTask(Task task, Utilizator membru) {
        task.setMembru(membru);
    }

    @Override
    public List<Task> creeazaTaskuriInSerie(List<String> descrieri, Date dataLimita) {
        List<Task> taskuri = new ArrayList<>();
        for (String descriere : descrieri) {
            Task task = this.creeazaTaskValidat(descriere, Status.NOU, dataLimita, null);
            taskuri.add(task);
        }
        return taskuri;
    }

    @Override
    public Task dinDTO(Task taskDTO) {
        return taskDTO;
    }
    @Override
    public void initializeazaEntitatiTask() {

    }
    @Override
    public boolean valideazaTask(Task task) {
        return task != null && task.getDescriere() != null && !task.getDescriere().isEmpty();
    }

    @Override
    public Task actualizeazaStatusTask(Task task, String statusNou) {
        task.setStatus(Status.NOU);
        return task;
    }
}
