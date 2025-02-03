package testservicii.WorkflowServices;

import jakarta.persistence.EntityManager;
import org.Proiect.Domain.App.StatusProiect;
import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.Status;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestEntityManager {

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void testEntityManager() {
        assertNotNull(em);

        Proiect proiect = new Proiect();
        proiect.setDenumire("Proiect Test");
        proiect.setDescriere("Descriere Proiect Test");
        proiect.setStatus(StatusProiect.IN_PROGRESS);
        proiect.setDataIncepere(new Date());
        em.persist(proiect);


        Utilizator lider = new Utilizator();
        lider.setNume("Lider Test");
        em.persist(lider);

        Utilizator membru = new Utilizator();
        membru.setNume("Membru Test");
        em.persist(membru);


        Task task = new Task();
        task.setDenumire("Task Test");
        task.setDescriere("Descriere task");
        task.setDataIncepere(new Date());
        task.setDataFinalizare(new Date());
        task.setStatus(Status.IN_EXECUTIE);
        task.setDeadline(LocalDate.now().plusDays(2));
        task.setProiect(proiect);
        task.setLider(lider);
        task.setMembru(membru);

        em.persist(task);

        // Interogarea pentru a obține toate task-urile
        List<Task> taskList = em.createQuery("SELECT t FROM Task t", Task.class)
                .getResultList();

        // Verificăm că există cel puțin un task în baza de date
        assertTrue(taskList.size() > 0, "Ar trebui să existe cel puțin un task");

        // Afișăm fiecare task
        for (Task t : taskList) {
            System.out.println("Task: " + t);
        }

        // Verificăm că task-ul salvat este prezent
        Task taskSalvat = em.find(Task.class, task.getTaskUserId());
        assertNotNull(taskSalvat, "Task-ul salvat trebuie să existe în baza de date");
        assertEquals(task.getDenumire(), taskSalvat.getDenumire(), "Denumirea task-ului ar trebui să fie aceeași");
        assertEquals(task.getDescriere(), taskSalvat.getDescriere(), "Descrierea task-ului ar trebui să fie aceeași");
    }
}
