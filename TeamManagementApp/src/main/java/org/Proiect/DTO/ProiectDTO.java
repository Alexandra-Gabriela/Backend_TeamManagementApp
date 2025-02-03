package org.Proiect.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.Proiect.Domain.App.StatusProiect;

import java.util.Date;

@Data
public class ProiectDTO {
    private int id;
    private String denumire;
    private String descriere;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dataIncepere;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dataFinalizare;


    private UtilizatorDTO lider;
}
