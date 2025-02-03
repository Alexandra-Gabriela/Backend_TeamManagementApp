package org.Proiect.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Setter @Getter
public class BadgeDTO {
    private Integer id;
    private String titlu;
    private String descriere;
    private int dificultate;
    private CursDTO cursId;

    public BadgeDTO() {
    }

    public BadgeDTO(Integer id, String titlu, String descriere, int dificultate, CursDTO cursId) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.dificultate = dificultate;

    }
}