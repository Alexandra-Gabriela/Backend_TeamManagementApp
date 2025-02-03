package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.IProiectFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProiectFactory implements IProiectFactory {
    @Override
    public Proiect creeazaProiectValidat(String denumire, String descriere, Utilizator lider, List<Echipa> echipe, Date dataIncepere) {
        if (denumire == null || denumire.isEmpty()) {
            throw new IllegalArgumentException("Denumirea proiectului nu poate fi goală.");
        }
        if (dataIncepere == null) {
            throw new IllegalArgumentException("Data de începere nu poate fi null.");
        }
        Proiect proiect = new Proiect();
        proiect.setDenumire(denumire);
        proiect.setDescriere(descriere);
        proiect.setLider(lider);
        proiect.setEchipe(echipe != null ? new ArrayList<>(echipe) : new ArrayList<>());  // Asigură-te că este mutabilă
        proiect.setDataIncepere(dataIncepere);
        return proiect;
    }

    @Override
    public Proiect adaugaEchipa(Proiect proiect, Echipa echipa) {
        if (proiect.getEchipe() == null) {
            proiect.setEchipe(new ArrayList<>());
        }
        if (!(proiect.getEchipe() instanceof ArrayList)) {
            proiect.setEchipe(new ArrayList<>(proiect.getEchipe()));
        }
        proiect.getEchipe().add(echipa);
        return proiect;
    }

    @Override
    public Proiect adaugaTaskuri(Proiect proiect, List<Task> taskuri) {
        proiect.getTasks().addAll(taskuri);
        return proiect;
    }

    @Override
    public Proiect arhiveazaProiect(Proiect proiect) {
        proiect.setStatus(StatusProiect.ON_HOLD);
        return proiect;
    }

    @Override
    public boolean valideazaProiect(Proiect proiect) {
        return proiect != null && proiect.getDenumire() != null && !proiect.getDenumire().isEmpty();
    }

    @Override
    public Proiect dinDTO(Proiect proiectDTO) {
        return proiectDTO;
    }

    @Override
    public Proiect creeazaProiectSablon(String tipSablon, Utilizator lider) {
        Proiect proiect = new Proiect();
        proiect.setDenumire("Proiect " + tipSablon);
        proiect.setDescriere("Proiect generat automat pe baza șablonului " + tipSablon);
        proiect.setLider(lider);
        proiect.setEchipe(new ArrayList<>());
        proiect.setTasks(new ArrayList<>());
        return proiect;
    }
}
