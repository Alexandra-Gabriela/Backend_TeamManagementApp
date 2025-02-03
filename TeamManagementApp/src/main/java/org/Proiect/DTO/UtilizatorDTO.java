package org.Proiect.DTO;

import lombok.Data;
import org.Proiect.Domain.App.TipUtilizator;

@Data
public class UtilizatorDTO {
    private int userId;
    private String nume;
    private TipUtilizator tipUtilizator;
    private String email;
}
