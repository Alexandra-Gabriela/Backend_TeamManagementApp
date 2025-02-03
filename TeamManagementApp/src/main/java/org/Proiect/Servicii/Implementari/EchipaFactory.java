package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Servicii.IEchipaFactory;
import org.springframework.stereotype.Service;

@Service
public class EchipaFactory implements IEchipaFactory {
    @Override
    public Echipa creeazaEchipa(String numeEchipa, Utilizator lider) {
        if (lider == null) {
            throw new IllegalArgumentException("Liderul nu poate fi null pentru a crea o echipă.");
        }
        Echipa echipa = new Echipa();
        echipa.setDenumire(numeEchipa);
        echipa.setLider(lider);
        echipa.setArhivata(false);
        return echipa;
    }

    @Override
    public Echipa creeazaEchipaFaraLider(String numeEchipa) {
        Echipa echipa = new Echipa();
        echipa.setDenumire(numeEchipa);
        echipa.setArhivata(false);
        return echipa;
    }

    @Override
    public Echipa creeazaEchipaArhivata(String numeEchipa, Utilizator lider) {
        if (lider == null) {
            throw new IllegalArgumentException("Liderul nu poate fi null pentru a crea o echipă arhivată.");
        }
        Echipa echipa = new Echipa();
        echipa.setDenumire(numeEchipa);
        echipa.setLider(lider);
        echipa.setArhivata(true);
        return echipa;
    }
    }

