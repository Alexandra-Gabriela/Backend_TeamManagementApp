package org.Proiect.Domain.Dezvoltare;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.Proiect.DTO.BadgeDTO;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "Titlul badge-ului este obligatoriu.")
    private String titlu;

    @ManyToOne
    @JoinColumn(name = "id_curs", nullable = false)
    private Curs curs;

    private String descriere;

    private int dificultate; // 1 = ușor, 5 = foarte greu
    public BadgeDTO toDTO() {
        BadgeDTO dto = new BadgeDTO();
        dto.setId(this.id);
        dto.setTitlu(this.titlu);
        dto.setDescriere(this.descriere);
        dto.setDificultate(this.dificultate);

        // Setează cursul fără a provoca recursivitate
        if (this.curs != null) {
            dto.setCursId(this.curs.toDTO()); // Mapăm cursul la un DTO
        }

        return dto;
    }


    // === Mapper: BadgeDTO -> Badge ===
    public static Badge fromDTO(BadgeDTO dto, Curs curs) {
        Badge badge = new Badge();
        badge.setId(dto.getId());
        badge.setTitlu(dto.getTitlu());
        badge.setDescriere(dto.getDescriere());
        badge.setDificultate(dto.getDificultate());
        badge.setCurs(curs); // Cursul trebuie obținut din serviciul relevant
        return badge;
    }
}
