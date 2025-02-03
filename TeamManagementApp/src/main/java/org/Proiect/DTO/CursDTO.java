package org.Proiect.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class CursDTO {
    private Integer id;
    private String titlu;
    private String descriere;
    private int durataOre;
    private UtilizatorDTO adminId;
    public CursDTO() {
    }

    public CursDTO(Integer id, String titlu, String descriere, int durataOre, UtilizatorDTO adminId) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.durataOre = durataOre;
        this.adminId = adminId;
    }
}
