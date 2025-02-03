package testeServiciiRest;

import org.Proiect.DTO.DepartamentDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_DepartamentRESTService {
    private static final Logger logger = Logger.getLogger(Test_DepartamentRESTService.class.getName());
    private static final String serviceURL = "http://localhost:8083/team/rest/servicii/departamente";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void test1_GetAllDepartments() {
        logger.info("DEBUG: GET all departments...");

        HttpHeaders headers = generateHeaders();

        List<DepartamentDTO> departamente = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<DepartamentDTO>>() {}
        ).getBody();

        departamente.forEach(d -> logger.info("Departament: " + d));
    }

    @Test
    @Order(2)
    public void test2_AddDepartment() {
        logger.info("DEBUG: ADD department...");
        HttpHeaders headers = generateHeaders();

        DepartamentDTO departamentDTO = new DepartamentDTO();
        departamentDTO.setNumeDepartament("Departament Test");

        DepartamentDTO createdDepartment = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(departamentDTO, headers),
                DepartamentDTO.class
        ).getBody();

        logger.info("Created department: " + createdDepartment);
    }

    @Test
    @Order(3)
    public void test3_UpdateDepartment() {
        logger.info("DEBUG: UPDATE department...");
        HttpHeaders headers = generateHeaders();

        List<DepartamentDTO> departamente = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<DepartamentDTO>>() {}
        ).getBody();

        if (!departamente.isEmpty()) {
            DepartamentDTO departamentDTO = departamente.get(0);
            departamentDTO.setNumeDepartament(departamentDTO.getNumeDepartament() + " Updated");

            restTemplate.exchange(
                    serviceURL + "/" + departamentDTO.getId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(departamentDTO, headers),
                    DepartamentDTO.class
            );
        }
    }

    @Test
    @Order(4)
    public void test4_DeleteDepartment() {
        logger.info("DEBUG: DELETE department...");
        HttpHeaders headers = generateHeaders();

        // Obține toate departamentele
        List<DepartamentDTO> departamente = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<DepartamentDTO>>() {}
        ).getBody();

        // Șterge fiecare departament
        for (DepartamentDTO departament : departamente) {
            restTemplate.exchange(
                    serviceURL + "/" + departament.getId(),
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers),
                    Void.class
            );
        }

        // Verifică dacă lista de departamente este acum goală
        departamente = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<DepartamentDTO>>() {}
        ).getBody();

        assertTrue(departamente.isEmpty(), "Fail to delete all departments!");
    }


    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
