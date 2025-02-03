package testeServiciiRest;

import org.Proiect.DTO.EchipaDTO;
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
public class Test_EchipaRESTService {
    private static final Logger logger = Logger.getLogger(Test_EchipaRESTService.class.getName());
    private static final String serviceURL = "http://localhost:8083/team/rest/servicii/echipe";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void test1_GetAllTeams() {
        logger.info("DEBUG: GET all teams...");

        HttpHeaders headers = generateHeaders();

        List<EchipaDTO> echipe = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<EchipaDTO>>() {}
        ).getBody();

        echipe.forEach(e -> logger.info("Echipa: " + e));
    }

    @Test
    @Order(2)
    public void test2_AddTeam() {
        logger.info("DEBUG: ADD team...");
        HttpHeaders headers = generateHeaders();

        EchipaDTO echipaDTO = new EchipaDTO();
        echipaDTO.setDenumire("Echipa Test");


        EchipaDTO createdTeam = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(echipaDTO, headers),
                EchipaDTO.class
        ).getBody();

        logger.info("Created team: " + createdTeam);
    }

    @Test
    @Order(3)
    public void test3_UpdateTeam() {
        logger.info("DEBUG: UPDATE team...");
        HttpHeaders headers = generateHeaders();

        List<EchipaDTO> echipe = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<EchipaDTO>>() {}
        ).getBody();

        if (!echipe.isEmpty()) {
            EchipaDTO echipaDTO = echipe.get(0);
            echipaDTO.setDenumire(echipaDTO.getDenumire() + " Updated");

            restTemplate.exchange(
                    serviceURL + "/" + echipaDTO.getIdEchipa(),
                    HttpMethod.PUT,
                    new HttpEntity<>(echipaDTO, headers),
                    EchipaDTO.class
            );
        }
    }

    @Test
    @Order(4)
    public void test4_DeleteTeam() {
        logger.info("DEBUG: DELETE team...");
        HttpHeaders headers = generateHeaders();

        List<EchipaDTO> echipe = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<EchipaDTO>>() {}
        ).getBody();

        for (EchipaDTO echipaDTO : echipe) {
            restTemplate.exchange(
                    serviceURL + "/" + echipaDTO.getIdEchipa(),
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers),
                    Void.class
            );
        }

        echipe = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<EchipaDTO>>() {}
        ).getBody();

        assertTrue(echipe.isEmpty(), "Failed to delete teams!");
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
