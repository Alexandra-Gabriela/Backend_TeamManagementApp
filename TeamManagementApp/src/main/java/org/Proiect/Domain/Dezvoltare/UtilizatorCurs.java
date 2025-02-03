package org.Proiect.Domain.Dezvoltare;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Proiect.DTO.UtilizatorCursDTO;
import org.Proiect.Domain.Angajati.Utilizator;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class UtilizatorCurs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_utilizator", nullable = false)
    private Utilizator utilizator;

    @ManyToOne
    @JoinColumn(name = "id_curs", nullable = false)
    private Curs curs;

    // Informații suplimentare
    private boolean completat;
    private int progres; // Exprimat în procente
    private Date dataInrolare;
    private Date dataFinalizare;

    public UtilizatorCursDTO toDTO() {
        UtilizatorCursDTO dto = new UtilizatorCursDTO();
        dto.setId(this.id);

        // Setează utilizatorul fără recursivitate
        if (this.utilizator != null) {
            dto.setUtilizatorId(this.utilizator.toDTO()); // Mapăm utilizatorul la un DTO
        }

        // Setează cursul fără recursivitate
        if (this.curs != null) {
            dto.setCursId(this.curs.getId()); // Setează doar ID-ul cursului
        }

        dto.setCompletat(this.completat);
        dto.setProgres(this.progres);
        dto.setDataInrolare(this.dataInrolare);
        dto.setDataFinalizare(this.dataFinalizare);

        return dto;
    }


    // === Mapper: UtilizatorCursDTO -> UtilizatorCurs ===
    public static UtilizatorCurs fromDTO(UtilizatorCursDTO dto, Utilizator utilizator, Curs curs) {
        UtilizatorCurs utilizatorCurs = new UtilizatorCurs();
        utilizatorCurs.setId(dto.getId());
        utilizatorCurs.setUtilizator(utilizator); // Utilizatorul trebuie obținut din serviciul relevant
        utilizatorCurs.setCurs(curs);            // Cursul trebuie obținut din serviciul relevant
        utilizatorCurs.setCompletat(dto.isCompletat());
        utilizatorCurs.setProgres(dto.getProgres());
        utilizatorCurs.setDataInrolare(dto.getDataInrolare());
        utilizatorCurs.setDataFinalizare(dto.getDataFinalizare());
        return utilizatorCurs;
    }
}
