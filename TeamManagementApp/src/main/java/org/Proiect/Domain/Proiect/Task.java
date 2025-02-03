package org.Proiect.Domain.Proiect;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.Proiect.DTO.ProiectDTO;
import org.Proiect.DTO.TaskDTO;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.App.StatusProiect;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int taskUserId;
    @NotBlank(message = "Denumirea task-ului este obligatorie.")
    @Size(max = 100, message = "Denumirea task-ului nu poate depăși 100 de caractere.")
    private String denumire;
    @Size(max = 255, message = "Descrierea nu poate depăși 255 de caractere.")
    private String descriere;
    @NotNull(message = "Data de început este obligatorie.")
    private Date dataIncepere;
    @NotNull(message = "Data de finalizare este obligatorie.")
    private Date dataFinalizare;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Statusul este obligatoriu.")
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "id_proiect")
    private Proiect proiect;

    @ManyToOne
    @JoinColumn(name = "id_lider")
    private Utilizator lider;

    @ManyToOne
    @JoinColumn(name = "membru_id")
    private Utilizator membru;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Notificare> notificari;


    @Override
    public String toString() {
        return "Task{" +
                "taskUserId=" + taskUserId +
                ", denumire='" + denumire + '\'' +
                ", descriere='" + descriere + '\'' +
                ", dataIncepere=" + dataIncepere +
                ", dataFinalizare=" + dataFinalizare +
                ", status=" + status +
                ", deadline=" + deadline +
                ", proiect=" + proiect +
                '}';
    }


    public TaskDTO toDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setIdTask(this.taskUserId);
        taskDTO.setDenumire(this.denumire);
        taskDTO.setDescriere(this.descriere);
        taskDTO.setStatus(this.status);
        taskDTO.setDeadline(this.deadline);

        if (this.membru != null) {
            taskDTO.setMembru(this.membru.toDTO());
        }
        return taskDTO;
    }

    public static Task fromDTO(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskUserId(taskDTO.getIdTask());
        task.setDenumire(taskDTO.getDenumire());
        task.setDescriere(taskDTO.getDescriere());
        task.setStatus(taskDTO.getStatus());
        task.setDeadline(taskDTO.getDeadline());

        if (taskDTO.getMembru() != null) {
            task.setMembru(Utilizator.fromDTO(taskDTO.getMembru()));
        }
        return task;
    }



}
