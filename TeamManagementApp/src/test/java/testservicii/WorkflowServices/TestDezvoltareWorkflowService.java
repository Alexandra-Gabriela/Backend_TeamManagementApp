package testservicii.WorkflowServices;

import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Dezvoltare.Curs;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.Proiect.Servicii.IDezvoltareWorkflowService;
import org.Proiect.SpringBootDomain_AplicatieDAM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringBootDomain_AplicatieDAM.class)
public class TestDezvoltareWorkflowService {

    @Autowired
    private IDezvoltareWorkflowService dezvoltareWorkflowService;

    @Autowired
    private AppUserRepository utilizatorRepository;

    private Integer adminId;

    @BeforeEach
    void setUp() {
        adminId = 1;

        // Crearea unui utilizator Admin pentru test
        Utilizator admin = new Utilizator();
        admin.setUserId(adminId);
        admin.setTipUtilizator(TipUtilizator.ADMIN);
        utilizatorRepository.save(admin);
    }

    @Test
    void testCreeazaCurs() {
        Utilizator admin1 = new Utilizator();
        admin1.setUserId(adminId);
        admin1.setTipUtilizator(TipUtilizator.ADMIN);
        utilizatorRepository.save(admin1);
        String titlu = "Programare Java Avansată";


        Utilizator admin = utilizatorRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Adminul nu există."));


        Curs cursCreat = dezvoltareWorkflowService.creeazaCurs(titlu, adminId);


        assertNotNull(cursCreat, "Cursul creat nu trebuie să fie null");
        assertEquals(titlu, cursCreat.getTitlu(), "Titlul cursului trebuie să fie cel specificat");
        assertNotNull(cursCreat.getId(), "Cursul creat trebuie să aibă un ID generat");
        assertNotNull(cursCreat.getAdmin(), "Cursul trebuie să aibă un administrator asociat");
        assertEquals(adminId, cursCreat.getAdmin().getUserId(), "Admin ID-ul trebuie să fie cel folosit pentru crearea cursului");
    }
}
