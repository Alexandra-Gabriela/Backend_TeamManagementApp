package testeServiciiRest;

import org.Proiect.DTO.TaskDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_TaskRESTService {
    private static final Logger logger = Logger.getLogger(Test_TaskRESTService.class.getName());
    private static final String serviceURL = "http://localhost:8083/team/rest/servicii/taskuri";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void test1_CreateTask() {
        logger.info("DEBUG: Test CREATE task...");
        HttpHeaders headers = generateHeaders();

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescriere("Task test");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8083/team/rest/servicii/taskuri",
                HttpMethod.POST,
                new HttpEntity<>(taskDTO, headers),
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void test2_AssignTask() {
        logger.info("DEBUG: Test ASSIGN task...");

        HttpHeaders headers = generateHeaders();

        // Create a task first
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescriere("Task pentru assign test");

        String createResponse = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(taskDTO, headers),
                String.class
        ).getBody();

        assertNotNull(createResponse, "Task creation failed for assign test!");

        // Extract task ID from the response (assuming it's the last part of the response string)
        Integer taskId = Integer.parseInt(createResponse.replaceAll("[^0-9]", ""));

        Integer membruId = 1; // Exemplu ID membru
        restTemplate.exchange(
                serviceURL + "/" + taskId + "/assign/" + membruId,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Void.class
        );

        logger.info("Task assigned successfully.");
    }

    @Test
    @Order(3)
    public void test3_UpdateTask() {
        logger.info("DEBUG: Test UPDATE task...");

        HttpHeaders headers = generateHeaders();

        // Create a task first
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescriere("Task pentru update test");

        String createResponse = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(taskDTO, headers),
                String.class
        ).getBody();

        assertNotNull(createResponse, "Task creation failed for update test!");

        // Extract task ID from the response
        Integer taskId = Integer.parseInt(createResponse.replaceAll("[^0-9]", ""));

        String newDescriere = "Descriere actualizata";
        restTemplate.exchange(
                serviceURL + "/" + taskId,
                HttpMethod.PUT,
                new HttpEntity<>(newDescriere, headers),
                Void.class
        );

        logger.info("Task updated successfully.");
    }

    @Test
    @Order(4)
    public void test4_DeleteTask() {
        logger.info("DEBUG: Test DELETE task...");

        HttpHeaders headers = generateHeaders();

        // Create a task first
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescriere("Task pentru delete test");

        String createResponse = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(taskDTO, headers),
                String.class
        ).getBody();

        assertNotNull(createResponse, "Task creation failed for delete test!");

        // Extract task ID from the response
        Integer taskId = Integer.parseInt(createResponse.replaceAll("[^0-9]", ""));

        restTemplate.exchange(
                serviceURL + "/" + taskId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        logger.info("Task deleted successfully.");
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
