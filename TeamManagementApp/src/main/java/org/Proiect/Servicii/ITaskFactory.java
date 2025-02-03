package org.Proiect.Servicii;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Task;

import java.util.Date;
import java.util.List;

public interface ITaskFactory {
    // Creează un task nou și validează datele de intrare
    Task creeazaTaskValidat(String descriere, Status status, Date dataFinalizare, Utilizator utilizator);

    // Creează un task în baza unui șablon predefinit
    Task creeazaTaskDinSablon(String sablon, Utilizator membru);
    // Asignează un task unui membru existent
    void asignareTask(Task task, Utilizator membru);
    // Creează mai multe taskuri în serie
    List<Task> creeazaTaskuriInSerie(List<String> descrieri, Date dataLimita);
    // Conversie DTO la Task
    Task dinDTO(Task taskDTO);
    // Inițializează entitățile task pentru domeniu
    void initializeazaEntitatiTask();
    // Validare task
    boolean valideazaTask(Task task);
    // Actualizează statusul unui task
    Task actualizeazaStatusTask(Task task, String statusNou);

}
