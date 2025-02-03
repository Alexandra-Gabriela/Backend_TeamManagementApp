package org.Proiect.Servicii;

import org.Proiect.DTO.TaskDTO;

import java.util.Date;
import java.util.List;

public interface ITaskWorkflowService {

    // Lider de echipă poate crea un task nou
    Integer creeazaTaskNou(String descriereTask, Date dataLimita);

    // Lider de echipă poate asigna un task unui membru
    void asignareTaskMembru(Integer taskId, Integer membruId);

    // Lider de echipă poate modifica un task existent
    void modificareTask(Integer taskId, String descriereNoua);

    // Lider de echipă poate șterge un task
    void stergereTask(Integer taskId);

    // Orice utilizator poate vizualiza un task
    String vizualizareTask(Integer taskId);

    // Membru echipă poate schimba statusul unui task
    void schimbareStatusTask(Integer taskId, String statusNou);

    // Sistemul trimite o notificare liderului când un task este trecut la 'done'
    void trimiteNotificareLider(Integer taskId);

    // Sistemul verifică și gestionează depășirea deadline-ului pentru un task
    void gestioneazaDepasireDeadline(Integer taskId);

    // Membrul de echipă este înscris automat la un curs în cazul depășirii repetate a deadline-ului
    void inrolareMembruLaCurs(Integer membruId);

    // Membrul finalizează un curs și primește un badge
    void finalizareCursSiBadge(Integer membruId);

    // Sistemul valorifică task-ul în puncte pentru membru dacă este completat înainte de deadline
    void valorificaTask(Integer taskId, Integer membruId);

    // Sistemul reatribuie un task altui membru dacă deadline-ul este depășit
    void reatribuireTask(Integer taskId, Integer membruIdNou);
    List<TaskDTO> getAllTasks();

}

