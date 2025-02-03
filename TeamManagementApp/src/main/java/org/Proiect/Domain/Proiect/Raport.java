package org.Proiect.Domain.Proiect;
import java.util.Date;import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.Proiect.Domain.App.TipRaport;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Raport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int idRaport;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tipul raportului este obligatoriu.")
    private TipRaport tip;
    @NotBlank(message = "Denumirea raportului este obligatorie.")
    private String denumire;
    @NotNull(message = "Data generării este obligatorie.")
    private Date dataGenerare;
    @ManyToOne
    @JoinColumn(name = "id_proiect")
    private Proiect proiect;

}

