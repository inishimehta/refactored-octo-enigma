package ca.gbc.goaltrackingservice;

import ca.gbc.goaltrackingservice.dto.GoalRequest;
import ca.gbc.goaltrackingservice.dto.GoalResponse;
import ca.gbc.goaltrackingservice.repository.GoalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class GoalTrackingServiceApplicationTests {

    // 🔹 Start MongoDB container automatically for all tests
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:7.0.5")
    );

    @LocalServerPort
    private Integer port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoalRepository goalRepository;

    @BeforeEach
    void setUp() {
        // Configure RestAssured to target the correct port
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Clear database before each test
        goalRepository.deleteAll();
    }

    // Helper: build a test goal request
    private GoalRequest getGoalRequest() {
        return new GoalRequest(
                "Meditate Daily",
                "Spend 10 minutes meditating every morning",
                LocalDate.of(2025, 12, 31),
                "in-progress",
                "mindfulness"
        );
    }

    // ✅ Smoke Test
    @Test
    void contextLoads() {
        assertTrue(mongoDBContainer.isRunning(), "MongoDB Testcontainer should be running");
        assertNotNull(port, "App port should not be null");
    }

    // ✅ POST: Create Goal
    @Test
    void createGoal_shouldReturn201AndSaveToDB() {
        GoalRequest goalRequest = getGoalRequest();

        given()
                .contentType(JSON)
                .body(goalRequest)
                .when()
                .post("/api/goals")
                .then()
                .statusCode(201);

        // Verify goal was saved
        assertEquals(1, goalRepository.findAll().size());
        var savedGoal = goalRepository.findAll().get(0);
        assertEquals("Meditate Daily", savedGoal.getTitle());
        assertEquals("mindfulness", savedGoal.getCategory());
    }

    // ✅ GET: Retrieve all goals
    @Test
    void getAllGoals_shouldReturnListOfGoals() {
        // Add one manually
        goalRepository.save(new ca.gbc.goaltrackingservice.model.Goal(
                null, "Exercise Weekly", "Go to gym twice a week",
                LocalDate.of(2025, 11, 30), "in-progress", "fitness"
        ));

        List<GoalResponse> goals = given()
                .when()
                .get("/api/goals")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", GoalResponse.class);

        assertEquals(1, goals.size());
        assertEquals("Exercise Weekly", goals.get(0).title());
    }

    // ✅ PUT: Update Goal
    @Test
    void updateGoal_shouldModifyExistingGoal() {
        // Create one goal first
        GoalRequest request = getGoalRequest();

        given()
                .contentType(JSON)
                .body(request)
                .when()
                .post("/api/goals")
                .then()
                .statusCode(201);

        String goalId = goalRepository.findAll().get(0).getGoalId();

        GoalRequest updatedRequest = new GoalRequest(
                "Updated Goal",
                "Changed description",
                LocalDate.of(2026, 1, 1),
                "in-progress",
                "wellness"
        );

        given()
                .contentType(JSON)
                .body(updatedRequest)
                .when()
                .put("/api/goals/" + goalId)
                .then()
                .statusCode(200);

        var updatedGoal = goalRepository.findById(goalId).orElseThrow();
        assertEquals("Updated Goal", updatedGoal.getTitle());
        assertEquals("wellness", updatedGoal.getCategory());
    }

    // ✅ PUT: Mark as Completed
    @Test
    void markGoalCompleted_shouldUpdateStatusToCompleted() {
        GoalRequest request = getGoalRequest();

        given()
                .contentType(JSON)
                .body(request)
                .when()
                .post("/api/goals")
                .then()
                .statusCode(201);

        String goalId = goalRepository.findAll().get(0).getGoalId();

        given()
                .when()
                .put("/api/goals/" + goalId + "/complete")
                .then()
                .statusCode(200);

        var completedGoal = goalRepository.findById(goalId).orElseThrow();
        assertEquals("completed", completedGoal.getStatus());
    }

    // ✅ DELETE: Delete Goal
    @Test
    void deleteGoal_shouldRemoveFromDatabase() {
        GoalRequest request = getGoalRequest();

        given()
                .contentType(JSON)
                .body(request)
                .when()
                .post("/api/goals")
                .then()
                .statusCode(201);

        String goalId = goalRepository.findAll().get(0).getGoalId();

        when()
                .delete("/api/goals/" + goalId)
                .then()
                .statusCode(204);

        assertTrue(goalRepository.findById(goalId).isEmpty());
    }
}
