package org.Proiect.Servicii;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;

import java.util.List;

public interface IEchipaWorkflowService {
    Echipa creeazaEchipa(String numeEchipa, Integer  liderId);
    void adaugaMembruInEchipa(Integer  echipaId, Integer  userId);
    void modificaEchipa(Integer  echipaId, String numeNou);
    void arhiveazaEchipa(Integer  echipaId);
    Echipa vizualizeazaEchipa(Integer  echipaId);
    List<Utilizator> vizualizeazaMembriiEchipa(Integer  echipaId);
    List<Echipa> getAllEchipe(); // Noua metodă adăugată

}
