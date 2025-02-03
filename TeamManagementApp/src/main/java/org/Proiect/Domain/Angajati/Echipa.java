package org.Proiect.Domain.Angajati;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.Proiect.Domain.Proiect.Proiect;


import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Echipa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int idEchipa;
    @NotBlank(message = "Denumirea echipei este obligatorie.")
    @Size(max = 100, message = "Denumirea echipei nu poate depăși 100 de caractere.")
    private String denumire;

    @OneToMany(mappedBy = "echipa", cascade = CascadeType.ALL)
    private List<Utilizator> membri;

    @ManyToOne
    @JoinColumn(name = "proiect_id")
    private Proiect proiect;
    private boolean arhivata;

    public boolean isArhivata() {
        return arhivata;
    }
    @ManyToOne
    @JoinColumn(name = "id_lider")
    private Utilizator lider;


}
