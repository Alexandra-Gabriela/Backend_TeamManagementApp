package testservicii.WorkflowServices;

import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.DepartamentRepository;
import org.Proiect.Servicii.IDepartamentWorkflowService;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestDepartamentWorkflowService {

    @Autowired
    private IDepartamentWorkflowService departamentWorkflowService;

    @Autowired
    private AppUserRepository utilizatorRepository;

    @Autowired
    private DepartamentRepository departamentRepository;

    @Test
    void testCreeazaDepartament() {
        Utilizator manager = new Utilizator();
        manager.setNume("Manager Test");
        manager.setEmail("manager@test.com");
        manager.setTipUtilizator(TipUtilizator.MANAGER);
        manager = utilizatorRepository.save(manager);
        utilizatorRepository.flush();

        assertNotNull(manager.getUserId(), "ID-ul managerului nu a fost generat corect.");

        Departament departament = departamentWorkflowService.creeazaDepartament("Departament IT", manager.getUserId());

        assertNotNull(departament);
        assertEquals("Departament IT", departament.getNumeDepartament());
        assertEquals(manager.getUserId(), departament.getManagerProiect().getUserId());
    }

    @Test
    @Transactional
    void testAdaugaUtilizatorInDepartament() {
        Departament departament = new Departament();
        departament.setNumeDepartament("Departament HR");
        departament = departamentRepository.save(departament);

        Utilizator utilizator = new Utilizator();
        utilizator.setNume("Test User");
        utilizator = utilizatorRepository.save(utilizator);

        departamentWorkflowService.adaugaUtilizatorInDepartament(departament.getId(), utilizator.getUserId(), "MEMBRU");

        Departament departamentActualizat = departamentRepository.findById(departament.getId()).orElseThrow();
        assertNotNull(departamentActualizat.getAngajati(), "Lista de angajați nu trebuie să fie null");
        assertTrue(departamentActualizat.getAngajati().contains(utilizator), "Departamentul nu conține utilizatorul adăugat");

        Utilizator utilizatorActualizat = utilizatorRepository.findById(utilizator.getUserId()).orElseThrow();
        assertEquals(departament.getId(), utilizatorActualizat.getDepartament().getId());
        assertEquals("MEMBRU", utilizatorActualizat.getRol());
    }

    @Test
    void testModificaDepartament() {
        Departament departament = new Departament();
        departament.setNumeDepartament("Departament Initial");
        departament = departamentRepository.save(departament);

        departamentWorkflowService.modificaDepartament(departament.getId(), "Departament Modificat");

        Departament departamentActualizat = departamentRepository.findById(departament.getId()).orElseThrow();
        assertEquals("Departament Modificat", departamentActualizat.getNumeDepartament());
    }

    @Test
    void testVizualizeazaDepartament() {
        Departament departament = new Departament();
        departament.setNumeDepartament("Departament Marketing");
        departament = departamentRepository.save(departament);

        Departament departamentVizualizat = departamentWorkflowService.vizualizeazaDepartament(departament.getId());

        assertNotNull(departamentVizualizat);
        assertEquals(departament.getId(), departamentVizualizat.getId());
        assertEquals("Departament Marketing", departamentVizualizat.getNumeDepartament());
    }

    @Test
    void testVizualizeazaMembriiDepartament() {
        Departament departament = new Departament();
        departament.setNumeDepartament("Departament Tehnic");
        departament = departamentRepository.save(departament);

        Utilizator utilizator = new Utilizator();
        utilizator.setNume("John Doe");
        utilizator.setDepartament(departament);
        utilizator = utilizatorRepository.save(utilizator);

        List<Utilizator> membri = departamentWorkflowService.vizualizeazaMembriiDepartament(departament.getId());

        assertNotNull(membri);
        assertFalse(membri.isEmpty());
        assertEquals(1, membri.size());
        assertEquals("John Doe", membri.get(0).getNume());
    }
}
