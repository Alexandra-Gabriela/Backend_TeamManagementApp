package org.Proiect.Domain.Dezvoltare;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.Proiect.DTO.CursDTO;
import org.Proiect.Domain.Angajati.Utilizator;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Curs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank(message = "Titlul cursului este obligatoriu.")
    @Size(max = 100, message = "Titlul cursului nu poate depăși 100 de caractere.")
    private String titlu;
    @OneToMany(mappedBy = "curs", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UtilizatorCurs> utilizatoriCursuri;
    @OneToMany(mappedBy = "curs", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Badge> badges;
    private String descriere;
    private int durataOre;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Utilizator admin;

    // Mapper: Curs -> CursDTO
    public CursDTO toDTO() {
        CursDTO dto = new CursDTO();
        dto.setId(this.id);
        dto.setTitlu(this.titlu);
        dto.setDescriere(this.descriere);
        dto.setDurataOre(this.durataOre);

        // Setează ID-ul adminului corect, fără apel recursiv
        if (this.admin != null) {
            dto.setAdminId(this.admin.toDTO()); // Mapăm utilizatorul admin la un DTO
        }

        return dto;
    }


    // === Mapper: CursDTO -> Curs ===
    public static Curs fromDTO(CursDTO dto, Utilizator admin) {
        Curs curs = new Curs();
        curs.setId(dto.getId());
        curs.setTitlu(dto.getTitlu());
        curs.setDescriere(dto.getDescriere());
        curs.setDurataOre(dto.getDurataOre());
        curs.setAdmin(admin); // Adminul trebuie obținut din serviciul relevant
        return curs;
    }
}
