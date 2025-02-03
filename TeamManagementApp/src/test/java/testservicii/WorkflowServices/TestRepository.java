package testservicii.WorkflowServices;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.EchipaRepository;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestRepository {
    private static final Logger logger = Logger.getLogger(TestRepository.class.getName());

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EchipaRepository echipaRepository;

    @Test
    @Order(1)
    public void testFindByDepartamentId() {
        Integer departamentId = 1;
        List<Utilizator> utilizatori = appUserRepository.findByDepartamentId(departamentId);
        assertNotNull(utilizatori, "Lista de utilizatori nu trebuie să fie null.");
        logger.info("Utilizatori găsiți pentru departament: " + utilizatori);
    }

    @Test
    @Order(2)
    public void testFindByNumeContaining() {
        String numePartial = "Popescu";
        List<Utilizator> utilizatori = appUserRepository.findByNumeContaining(numePartial);
        assertNotNull(utilizatori, "Lista de utilizatori nu trebuie să fie null.");
        logger.info("Utilizatori găsiți după nume parțial: " + utilizatori);
    }

    @Test
    @Order(3)
    public void testFindByEmail() {
        Utilizator utilizator = new Utilizator();
        utilizator.setEmail("email@example.com");
        utilizator.setNume("Test User");
        utilizator.setDisponibil(true);
        utilizator.setTipUtilizator(TipUtilizator.MEMBRUECHIPA);
        utilizator = appUserRepository.save(utilizator);

        assertNotNull(utilizator.getUserId(), "Utilizatorul nu a fost salvat.");

        Optional<Utilizator> foundUtilizator = appUserRepository.findByEmail("email@example.com");
        assertTrue(foundUtilizator.isPresent(), "Utilizatorul nu a fost găsit.");
        logger.info("Utilizator găsit după email: " + foundUtilizator.get());
    }

  /*  @Test
    @Order(4)
    public void testFindByTipUtilizator() {
        TipUtilizator tip = TipUtilizator.LIDER;
        List<Utilizator> utilizatori = appUserRepository.findUtilizatorByTip(TI);
        assertNotNull(utilizatori, "Lista de utilizatori nu trebuie să fie null.");
        logger.info("Utilizatori găsiți după tip: " + utilizatori);
    }*/

    @Test
    @Order(5)
    public void testFindLideriByEchipaDenumire() {
        String denumireEchipa = "Echipa A";
        List<Utilizator> lideri = appUserRepository.findLideriByEchipaDenumire(denumireEchipa);
        assertNotNull(lideri, "Lista de lideri nu trebuie să fie null.");
        logger.info("Lideri găsiți după denumirea echipei: " + lideri);
    }

    @Test
    @Order(6)
    public void testFindMembriByEchipaId() {
        Integer echipaId = 2;
        List<Utilizator> membri = appUserRepository.findMembriByEchipaId(echipaId);
        assertNotNull(membri, "Lista de membri nu trebuie să fie null.");
        logger.info("Membri găsiți pentru echipă: " + membri);
    }

    @Test
    @Order(7)
    public void testFindMembriWithAssignedTasks() {
        List<Utilizator> membriCuTaskuri = appUserRepository.findMembriWithAssignedTasks();
        assertNotNull(membriCuTaskuri, "Lista de membri cu task-uri nu trebuie să fie null.");
        logger.info("Membri cu task-uri asignate: " + membriCuTaskuri);
    }


    @Test
    @Order(9)
    public void testFindAllByEchipa() {
        Integer echipaId = 4;
        List<Utilizator> utilizatori = appUserRepository.findAllByEchipa(echipaId);
        assertNotNull(utilizatori, "Lista de utilizatori nu trebuie să fie null.");
        logger.info("Utilizatori găsiți în echipă: " + utilizatori);
    }

    @Test
    @Order(10)
    public void testFindUtilizatoriByCursId() {
        Integer cursId = 6;
        List<Utilizator> utilizatori = appUserRepository.findUtilizatoriByCursId(cursId);
        assertNotNull(utilizatori, "Lista de utilizatori nu trebuie să fie null.");
        logger.info("Utilizatori găsiți pentru curs: " + utilizatori);
    }

    @Test
    @Order(11)
    public void testFindUtilizatoriNotInCurs() {
        Integer cursId = 7;
        List<Utilizator> utilizatori = appUserRepository.findUtilizatoriNotInCurs(cursId);
        assertNotNull(utilizatori, "Lista de utilizatori nu trebuie să fie null.");
        logger.info("Utilizatori care nu sunt în curs: " + utilizatori);
    }


}
