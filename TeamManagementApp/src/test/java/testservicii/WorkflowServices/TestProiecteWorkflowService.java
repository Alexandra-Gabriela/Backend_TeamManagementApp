package testservicii.WorkflowServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.EchipaRepository;
import org.Proiect.Servicii.Repository.TaskRepository;
import org.Proiect.Servicii.IEchipaFactory;
import org.Proiect.Servicii.IProiectFactory;
import org.Proiect.Servicii.IProiecteWorkflowService;
import org.Proiect.Servicii.ITaskFactory;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestProiecteWorkflowService {

    private static final Logger logger = Logger.getLogger(TestProiecteWorkflowService.class.getName());

    @Autowired
    private IProiecteWorkflowService proiecteWorkflowService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EchipaRepository echipaRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private IProiectFactory proiectFactory;
    @Autowired
    private IEchipaFactory echipaFactory;
    @Autowired
    private ITaskFactory taskFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testWorkflowServiceFacade() {
        logger.info("Starting Workflow Test...");

        // 1. Creare lider
        Utilizator lider = new Utilizator();
        lider.setNume("Lider Test");
        lider.setTipUtilizator(TipUtilizator.LIDER);
        lider = appUserRepository.save(lider);

        // 2. Creare echipă
        Echipa echipa = echipaFactory.creeazaEchipa("Echipa Test", lider);
        echipa = echipaRepository.save(echipa);

        // 3. Creare proiect
        List<Echipa> echipe = new ArrayList<>();
        echipe.add(echipa);

        Proiect proiect = proiecteWorkflowService.creareProiect(
                "Proiect Workflow Test",
                "Descriere Workflow Test",
                lider,
                echipe,
                new Date()
        );
        logger.info("Proiect creat: " + proiect.getDenumire());

        // 4. Adăugare echipă nouă
        Echipa echipaNoua = echipaFactory.creeazaEchipaFaraLider("Echipa Nouă");
        echipaNoua = echipaRepository.save(echipaNoua);
        proiect = proiecteWorkflowService.adaugaEchipaLaProiect(proiect.getId(), echipaNoua);
        logger.info("Echipa adăugată: " + echipaNoua.getDenumire());

        // 5. Creare membru și task
        Utilizator membru = new Utilizator();
        membru.setNume("Membru Test");
        membru.setTipUtilizator(TipUtilizator.MEMBRUECHIPA);
        membru = appUserRepository.save(membru);

        Task task = taskFactory.creeazaTaskValidat("Task Workflow", null, new Date(), membru);
        task = taskRepository.save(task);

        // 6. Atribuire task
        Task taskAtribuit = proiecteWorkflowService.atribuieTaskMembru(task.getTaskUserId(), membru);
        logger.info("Task-ul " + taskAtribuit.getDescriere() + " atribuit membrului: " + membru.getNume());
    }

}