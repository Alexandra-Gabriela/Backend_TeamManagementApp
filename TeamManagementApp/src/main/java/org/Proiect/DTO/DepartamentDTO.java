package org.Proiect.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DepartamentDTO {
    private int id;
    private String numeDepartament;
    private int managerId; // ID-ul managerului proiectului
    private String managerNume; // Numele managerului proiectului
    private List<Integer> angajatiIds; // Lista de ID-uri ale angajaților
}
