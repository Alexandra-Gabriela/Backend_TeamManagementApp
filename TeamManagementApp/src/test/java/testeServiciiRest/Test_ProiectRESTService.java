package testeServiciiRest;


import org.Proiect.DTO.ProiectDTO;
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

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_ProiectRESTService {
    private static final Logger logger = Logger.getLogger(Test_ProiectRESTService.class.getName());
    private static final String serviceURL = "http://localhost:8083/team/rest/servicii/proiect";
    private final RestTemplate restTemplate = new RestTemplate();
    @Test
    @Order(1)
    public void test1_GetAllProjects() {
        logger.info("DEBUG: Received GET request for all projects...");

        HttpHeaders headers = generateHeaders();

        List<ProiectDTO> proiecte = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<ProiectDTO>>() {}
        ).getBody();

        proiecte.forEach(p -> logger.info("Proiect: " + p));
    }

    @Test
    @Order(2)
    public void test2_AddProjects() {
        logger.info("DEBUG: Test ADD projects...");
        HttpHeaders headers = generateHeaders();

        ProiectDTO proiectDTO = new ProiectDTO();
        proiectDTO.setDenumire("Proiect Test");
        proiectDTO.setDescriere("Descriere test");
        proiectDTO.setStatus("IN_PROGRESS");
        proiectDTO.setDataIncepere(new Date());

        ProiectDTO createdProject = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(proiectDTO, headers),
                ProiectDTO.class
        ).getBody();

        logger.info("Created project: " + createdProject);
    }

    @Test
    @Order(3)
    public void test3_UpdateProject() {
        logger.info("DEBUG: Test UPDATE project...");
        HttpHeaders headers = generateHeaders();

        // Get all projects
        List<ProiectDTO> proiecte = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<ProiectDTO>>() {}
        ).getBody();

        if (!proiecte.isEmpty()) {
            ProiectDTO proiectDTO = proiecte.get(0);
            proiectDTO.setDenumire(proiectDTO.getDenumire() + " Updated");

            restTemplate.exchange(
                    serviceURL + "/" + proiectDTO.getId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(proiectDTO, headers),
                    ProiectDTO.class
            );
        }
    }

    @Test
    @Order(4)
    public void test4_DeleteProjects() {
        logger.info("DEBUG: Test DELETE projects...");
        HttpHeaders headers = generateHeaders();

        List<ProiectDTO> proiecte = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<ProiectDTO>>() {}
        ).getBody();

        for (ProiectDTO proiectDTO : proiecte) {
            restTemplate.exchange(
                    serviceURL + "/" + proiectDTO.getId(),
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers),
                    Void.class
            );
        }

        proiecte = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<ProiectDTO>>() {}
        ).getBody();

        assertTrue(proiecte.isEmpty(), "Fail to delete projects!");
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
