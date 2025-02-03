package org.Proiect.Domain.Angajati;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor


public class Departament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @NotBlank(message = "Numele departamentului este obligatoriu.")
    @Size(max = 100, message = "Numele departamentului nu poate depăși 100 de caractere.")
    private String numeDepartament;
    @ManyToOne
    @JoinColumn(name = "id_manager_proiect")
    private Utilizator managerProiect;


    @OneToMany(mappedBy = "departament", cascade = CascadeType.ALL)
    private List<Utilizator> angajati=new ArrayList<>();

}
