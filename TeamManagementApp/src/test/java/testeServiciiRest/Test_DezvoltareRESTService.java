package testeServiciiRest;

import org.Proiect.DTO.BadgeDTO;
import org.Proiect.DTO.CursDTO;
import org.Proiect.DTO.UtilizatorCursDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_DezvoltareRESTService {
    private static final Logger logger = Logger.getLogger(Test_DezvoltareRESTService.class.getName());
    private static final String serviceURL = "http://localhost:8083/team/rest/servicii/dezvoltare";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void test1_GetAllBadges() {
        logger.info("DEBUG: Received GET request for all badges...");

        HttpHeaders headers = generateHeaders();

        List<BadgeDTO> badges = restTemplate.exchange(
                serviceURL + "/badge",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<BadgeDTO>>() {}
        ).getBody();

        badges.forEach(b -> logger.info("Badge: " + b));
    }

    @Test
    @Order(2)
    public void test2_CreateCourse() {
        logger.info("DEBUG: Test ADD course...");
        HttpHeaders headers = generateHeaders();

        CursDTO cursDTO = new CursDTO();
        cursDTO.setTitlu("Curs Test");
        cursDTO.setAdminId(1); // Assumed adminId for testing

        CursDTO createdCourse = restTemplate.exchange(
                serviceURL + "/cursuri",
                HttpMethod.POST,
                new HttpEntity<>(cursDTO, headers),
                CursDTO.class
        ).getBody();

        logger.info("Created course: " + createdCourse);
    }

    @Test
    @Order(3)
    public void test3_TrackUserProgress() {
        logger.info("DEBUG: Test TRACK user progress...");
        HttpHeaders headers = generateHeaders();

        // Assumed courseId and userId for testing
        int cursId = 1;
        int utilizatorId = 1;

        UtilizatorCursDTO progresDTO = restTemplate.exchange(
                serviceURL + "/utilizator-progres?cursId=" + cursId + "&utilizatorId=" + utilizatorId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UtilizatorCursDTO.class
        ).getBody();

        logger.info("User progress: " + progresDTO);
    }

    @Test
    @Order(4)
    public void test4_GenerateBadge() {
        logger.info("DEBUG: Test GENERATE badge...");
        HttpHeaders headers = generateHeaders();

        // Assumed courseId and userId for testing
        int cursId = 1;
        int utilizatorId = 1;

        BadgeDTO badgeDTO = restTemplate.exchange(
                serviceURL + "/badge?cursId=" + cursId + "&utilizatorId=" + utilizatorId,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                BadgeDTO.class
        ).getBody();

        logger.info("Generated badge: " + badgeDTO);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}