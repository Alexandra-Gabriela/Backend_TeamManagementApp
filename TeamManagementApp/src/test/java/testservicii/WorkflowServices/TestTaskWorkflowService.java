package testservicii.WorkflowServices;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.Repository.EchipaRepository;
import org.Proiect.Servicii.Repository.RepositoryProiect;
import org.Proiect.Servicii.Repository.TaskRepository;
import org.Proiect.Servicii.IEchipaFactory;
import org.Proiect.Servicii.ITaskFactory;
import org.Proiect.Servicii.ITaskWorkflowService;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestTaskWorkflowService {
    @Autowired
    private ITaskWorkflowService taskWorkflowService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ITaskFactory taskFactory;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private RepositoryProiect repositoryProiect;
    @Autowired
    private IEchipaFactory echipaFactory;
    @Autowired
    private EchipaRepository echipaRepository;


    @Test
    void testGestioneazaDepasireDeadline() {
        Utilizator lider = creeazaUtilizator("Lider Echipa", 0, TipUtilizator.LIDER, null);
        Echipa echipa = echipaFactory.creeazaEchipa("Echipa Test", lider);
        echipa = echipaRepository.save(echipa);

        Utilizator membru = creeazaUtilizator("Test User", 2, TipUtilizator.MEMBRUECHIPA, echipa);

        Task task = taskFactory.creeazaTaskValidat(
                "Test Task",
                Status.NOU,
                new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), // Deadline depășit
                membru
        );
        task = taskRepository.save(task);

        taskWorkflowService.gestioneazaDepasireDeadline(task.getTaskUserId());

        Task updatedTask = taskRepository.findById(task.getTaskUserId()).orElseThrow();
        assertEquals(task.getTaskUserId(), updatedTask.getTaskUserId());
        assertEquals(membru.getUserId(), updatedTask.getMembru().getUserId()); // Membrul rămâne același
    }

    @Test
    void testGestioneazaDepasireDeadline_CuDepasiriMultiple() {

        Utilizator lider = creeazaUtilizator("Lider Proiect", 0, TipUtilizator.LIDER, null);

        Proiect proiect = new Proiect();
        proiect.setDenumire("Test Proiect");
        proiect.setDescriere("Descriere proiect test");
        proiect.setDataIncepere(new Date());
        proiect.setStatus(StatusProiect.IN_PROGRESS);
        proiect = repositoryProiect.save(proiect);

        Echipa echipa = echipaFactory.creeazaEchipa("Echipa Test", lider);
        echipa.setProiect(proiect);
        echipa = echipaRepository.save(echipa);

        Utilizator membruDepasit = creeazaUtilizator("Test User 2", 6, TipUtilizator.MEMBRUECHIPA, echipa);
        Utilizator altMembruDisponibil = creeazaUtilizator("Alt User", 0, TipUtilizator.MEMBRUECHIPA, echipa);
        altMembruDisponibil.setDisponibil(true);
        altMembruDisponibil = appUserRepository.save(altMembruDisponibil);

        Task task = taskFactory.creeazaTaskValidat(
                "Test Task 2",
                Status.IN_EXECUTIE,
                new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), // Deadline depășit
                membruDepasit
        );
        task.setProiect(proiect);
        task = taskRepository.save(task);

        // Act
        taskWorkflowService.gestioneazaDepasireDeadline(task.getTaskUserId());

        // Assert
        Task updatedTask = taskRepository.findById(task.getTaskUserId()).orElseThrow();
        assertNotEquals(membruDepasit.getUserId(), updatedTask.getMembru().getUserId()); // Task-ul a fost reatribuit
        assertEquals(altMembruDisponibil.getUserId(), updatedTask.getMembru().getUserId()); // Task-ul este acum la alt membru
    }

    private Utilizator creeazaUtilizator(String nume, int depasiriDeadline, TipUtilizator tip, Echipa echipa) {
        Utilizator utilizator = new Utilizator();
        utilizator.setNume(nume);
        utilizator.setDepasiriDeadline(depasiriDeadline);
        utilizator.setTipUtilizator(tip);
        utilizator.setEchipa(echipa);
        return appUserRepository.save(utilizator);
    }
}