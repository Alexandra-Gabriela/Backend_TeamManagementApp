package org.Proiect.Domain.Proiect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.Proiect.DTO.ProiectDTO;
import org.Proiect.Domain.Angajati.Echipa;

import jakarta.persistence.*;
import lombok.*;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.StatusProiect;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Proiect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = "Denumirea proiectului este obligatorie.")
    @Size(max = 100, message = "Denumirea proiectului nu poate depăși 100 de caractere.")
    private String denumire;

    @Size(max = 255, message = "Descrierea proiectului nu poate depăși 255 de caractere.")
    private String descriere;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProiect status = StatusProiect.CREATED;

    @Column(nullable = false)
    private Date dataIncepere;

    private Date dataFinalizare;

    @JsonManagedReference
    @OneToMany(mappedBy = "proiect", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Utilizator lider;

    @OneToMany(mappedBy = "proiect", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Raport> rapoarte;

    @OneToMany(mappedBy = "proiect", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Echipa> echipe;

    @JsonIgnore
    @Transient
    private int taskCount;


    // === Mapper: Proiect -> ProiectDTO ===
    public ProiectDTO toDTO() {
        ProiectDTO dto = new ProiectDTO();
        dto.setId(this.id);
        dto.setDenumire(this.denumire);
        dto.setDescriere(this.descriere);
        dto.setStatus(this.status != null ? this.status.toString() : null);
        dto.setDataIncepere(this.dataIncepere);
        dto.setDataFinalizare(this.dataFinalizare);
        return dto;
    }


    public static Proiect fromDTO(ProiectDTO dto) {
        Proiect proiect = new Proiect();
        proiect.setId(dto.getId());
        proiect.setDenumire(dto.getDenumire());
        proiect.setDescriere(dto.getDescriere());
        proiect.setStatus(dto.getStatus() != null ? StatusProiect.valueOf(dto.getStatus()) : null);
        proiect.setDataIncepere(dto.getDataIncepere());
        proiect.setDataFinalizare(dto.getDataFinalizare());
        return proiect;
    }
}
