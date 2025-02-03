package org.Proiect.Servicii;

import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Raport;
import org.Proiect.Domain.Proiect.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface IProiecteWorkflowService {
   // Creare proiect
   Proiect creareProiect(String denumire, String descriere, Utilizator lider, List<Echipa> echipe, Date dataInceput);

   // Modificare lider proiect
   Proiect modificaTeamLeader(Integer proiectId, Utilizator nouLider);

   // Atribuire echipe
   @Transactional
   Proiect adaugaEchipaLaProiect(Integer proiectId, Echipa echipa);

   // Atribuire taskuri
   Task atribuieTaskMembru(Integer taskId, Utilizator membru);

   // Management status proiect
   Proiect actualizareStatusProiect(Integer proiectId, StatusProiect status);
   Proiect finalizareProiect(Integer proiectId);

   // Monitorizare și rapoarte
   List<Task> monitorizareTaskuri(Integer proiectId);
   Raport genereazaRaport(Integer proiectId);

   // Ștergere
   boolean stergeProiect(Integer proiectId);

   // Arhivare
   Proiect arhiveazaProiect(Integer proiectId);

   // Obținere date
   Proiect getProiectById(Integer proiectId);
   List<Proiect> getToateProiectele();
}
