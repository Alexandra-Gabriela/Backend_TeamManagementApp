package org.Proiect.Servicii.Implementari;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.EchipaRepository;
import org.Proiect.Servicii.IEchipaFactory;
import org.Proiect.Servicii.IEchipaWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class EchipaWorkflowService implements IEchipaWorkflowService {

    @Autowired
    private EchipaRepository echipaRepository;

    @Autowired
    private AppUserRepository utilizatorRepository;

    @Autowired
    private IEchipaFactory echipaFactory; // Injectăm factory-ul

    @Override
    public Echipa creeazaEchipa(String numeEchipa, Integer liderId) {
        // Găsim liderul
        Utilizator lider = utilizatorRepository.findById(liderId)
                .orElseThrow(() -> new IllegalArgumentException("Liderul nu există"));

        // Verificăm că utilizatorul este un lider
        if (lider.getTipUtilizator() != TipUtilizator.LIDER) {
            throw new IllegalArgumentException("Doar liderii pot crea echipe");
        }

        // Folosim factory-ul pentru a crea echipa
        Echipa echipa = echipaFactory.creeazaEchipa(numeEchipa, lider);

        // Salvăm echipa în baza de date
        return echipaRepository.save(echipa);
    }

    @Override
    public void adaugaMembruInEchipa(Integer echipaId, Integer userId) {
        // Găsim echipa și utilizatorul
        Echipa echipa = echipaRepository.findById(echipaId)
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu există"));
        Utilizator utilizator = utilizatorRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu există"));

        // Adăugăm utilizatorul în echipă
        utilizator.setEchipa(echipa);
        utilizatorRepository.save(utilizator);
    }

    @Override
    public void modificaEchipa(Integer echipaId, String numeNou) {
        // Găsim echipa
        Echipa echipa = echipaRepository.findById(echipaId)
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu există"));

        // Modificăm numele echipei
        echipa.setDenumire(numeNou);
        echipaRepository.save(echipa);
    }

    @Override
    @Transactional
    public void arhiveazaEchipa(Integer echipaId) {
        // Găsim echipa
        Echipa echipa = echipaRepository.findById(echipaId)
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu există"));

        // Verificăm dacă echipa este deja arhivată
        if (echipa.isArhivata()) {
            throw new IllegalArgumentException("Echipa este deja arhivată");
        }

        // Folosim factory-ul pentru a crea o echipă arhivată (dacă avem reguli speciale)
        Echipa echipaArhivata = echipaFactory.creeazaEchipaArhivata(echipa.getDenumire(), echipa.getLider());

        // Marcăm echipa ca arhivată
        echipa.setArhivata(true);
        echipaRepository.save(echipa);
    }

    @Override
    public Echipa vizualizeazaEchipa(Integer echipaId) {
        return echipaRepository.findById(echipaId)
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu există"));
    }

    @Override
    public List<Utilizator> vizualizeazaMembriiEchipa(Integer echipaId) {
        // Găsim echipa
        Echipa echipa = echipaRepository.findById(echipaId)
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu există"));

        // Obținem membrii echipei
        return utilizatorRepository.findAllByEchipa(echipaId);
    }
    @Override
    public List<Echipa> getAllEchipe() {
        // Implementare pentru a returna toate departamentele
        return echipaRepository.findAll();
    }
}
