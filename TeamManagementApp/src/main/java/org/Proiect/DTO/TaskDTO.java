package org.Proiect.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.Proiect.Domain.App.Status;

import java.time.LocalDate;

@Data
public class TaskDTO {

    private int idTask;
    private String denumire;
    private String descriere;
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @NotNull(message = "Task must have an assigned user")
    private UtilizatorDTO membru;

    public TaskDTO(int idTask, String denumire, String descriere, Status status, LocalDate deadline, UtilizatorDTO membru) {
        this.idTask = idTask;
        this.denumire = denumire;
        this.descriere = descriere;
        this.status = status;
        this.deadline = deadline;
        this.membru = membru;
    }

    public TaskDTO() {
    }

    // Getters și Setters
    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UtilizatorDTO getMembru() {
        return membru;
    }

    public void setMembru(UtilizatorDTO membru) {
        this.membru = membru;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "idTask=" + idTask +
                ", denumire='" + denumire + '\'' +
                ", descriere='" + descriere + '\'' +
                ", status=" + status +
                ", deadline=" + deadline +
                ", membru=" + membru +
                '}';
    }
}
