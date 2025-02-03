package org.Proiect.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data @Getter @Setter
public class UtilizatorCursDTO {
    private Long id;
    private UtilizatorDTO utilizatorId;
    private Integer cursId;
    private boolean completat;
    private int progres;
    private Date dataInrolare;
    private Date dataFinalizare;

    public UtilizatorCursDTO() {
    }

    public UtilizatorCursDTO(Long id, UtilizatorDTO utilizatorId, Integer cursId, boolean completat, int progres, Date dataInrolare, Date dataFinalizare) {
        this.id = id;
        this.utilizatorId = utilizatorId;
        this.cursId = cursId;
        this.completat = completat;
        this.progres = progres;
        this.dataInrolare = dataInrolare;
        this.dataFinalizare = dataFinalizare;
    }
}
