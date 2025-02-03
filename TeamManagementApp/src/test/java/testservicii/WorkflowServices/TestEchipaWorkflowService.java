package testservicii.WorkflowServices;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.IEchipaWorkflowService;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestEchipaWorkflowService {

    @Autowired
    private IEchipaWorkflowService echipaWorkflowService;

    @Autowired
    private AppUserRepository utilizatorRepository;

    @Test
    public void testCreeazaEchipa() {
        // Creăm liderul
        Utilizator lider = new Utilizator();
        lider.setNume("Lider Test");
        lider.setTipUtilizator(TipUtilizator.LIDER);
        lider = utilizatorRepository.save(lider);

        // Creăm echipa folosind workflow-ul
        Echipa echipa = echipaWorkflowService.creeazaEchipa("Echipa Test", lider.getUserId());

        // Validăm rezultatul
        assertNotNull(echipa);
        assertEquals("Echipa Test", echipa.getDenumire());
        assertEquals(lider.getUserId(), echipa.getLider().getUserId());
    }

    @Test
    public void testAdaugaMembruInEchipa() {
        // Creăm liderul
        Utilizator lider = new Utilizator();
        lider.setNume("Lider Test");
        lider.setTipUtilizator(TipUtilizator.LIDER);
        lider = utilizatorRepository.save(lider);

        // Creăm echipa
        Echipa echipa = echipaWorkflowService.creeazaEchipa("Echipa Test", lider.getUserId());

        // Creăm un membru și îl adăugăm în echipă
        Utilizator membru = new Utilizator();
        membru.setNume("Membru Test");
        membru.setTipUtilizator(TipUtilizator.MEMBRUECHIPA);
        membru = utilizatorRepository.save(membru);

        echipaWorkflowService.adaugaMembruInEchipa(echipa.getIdEchipa(), membru.getUserId());

        // Validăm că membrul a fost adăugat corect
        List<Utilizator> membri = echipaWorkflowService.vizualizeazaMembriiEchipa(echipa.getIdEchipa());
        assertEquals(1, membri.size());
        assertEquals(membru.getUserId(), membri.get(0).getUserId());
    }

    @Test
    public void testModificaEchipa() {
        // Creăm liderul
        Utilizator lider = new Utilizator();
        lider.setNume("Lider Test");
        lider.setTipUtilizator(TipUtilizator.LIDER);
        lider = utilizatorRepository.save(lider);

        // Creăm echipa
        Echipa echipa = echipaWorkflowService.creeazaEchipa("Echipa Test", lider.getUserId());

        // Modificăm numele echipei
        echipaWorkflowService.modificaEchipa(echipa.getIdEchipa(), "Echipa Modificata");

        // Validăm că modificarea s-a realizat corect
        Echipa echipaModificata = echipaWorkflowService.vizualizeazaEchipa(echipa.getIdEchipa());
        assertEquals("Echipa Modificata", echipaModificata.getDenumire());
    }

    @Test
    public void testArhiveazaEchipa() {
        // Creăm liderul
        Utilizator lider = new Utilizator();
        lider.setNume("Lider Test");
        lider.setTipUtilizator(TipUtilizator.LIDER);
        lider = utilizatorRepository.save(lider);

        // Creăm echipa
        Echipa echipa = echipaWorkflowService.creeazaEchipa("Echipa Test", lider.getUserId());

        // Arhivăm echipa
        echipaWorkflowService.arhiveazaEchipa(echipa.getIdEchipa());

        // Validăm că echipa este arhivată
        Echipa echipaArhivata = echipaWorkflowService.vizualizeazaEchipa(echipa.getIdEchipa());
        assertTrue(echipaArhivata.isArhivata());
    }
}
