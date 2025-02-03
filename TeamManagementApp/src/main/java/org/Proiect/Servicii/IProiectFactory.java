package org.Proiect.Servicii;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Proiect.Task;

import java.util.Date;
import java.util.List;

public interface IProiectFactory {
    // Creează un proiect validat
    Proiect creeazaProiectValidat(String denumire, String descriere, Utilizator lider, List<Echipa> echipe, Date dataIncepere);
    // Adaugă o echipă la proiect
    Proiect adaugaEchipa(Proiect proiect, Echipa echipa);
    // Adaugă taskuri la proiect
    Proiect adaugaTaskuri(Proiect proiect, List<Task> taskuri);
    // Arhivează un proiect existent
    Proiect arhiveazaProiect(Proiect proiect);
    // Validează un proiect
    boolean valideazaProiect(Proiect proiect);
    // Inițializează un proiect nou dintr-un DTO
    Proiect dinDTO(Proiect proiectDTO);
    // Creează un proiect șablon (cu date implicite)
    Proiect creeazaProiectSablon(String tipSablon, Utilizator lider);
}
