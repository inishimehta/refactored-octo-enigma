package ca.gbc.comp3095.eventservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class EventServiceApplicationTests {

    // 🔹 Start a temporary PostgreSQL container
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("eventdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    // 🔹 Dynamically configure Spring Boot to use the Testcontainer database
    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    private String newEventJson;

    @BeforeEach
    void setup() {
        newEventJson = """
                {
                    "title": "Stress Management Workshop",
                    "location": "Room 101",
                    "date": "2025-11-20",
                    "time": "14:00",
                    "description": "Workshop to manage stress effectively"
                }
                """;
    }

    @Test
    void contextLoads() {
        // Ensure the PostgreSQL container started properly
        assert(postgres.isRunning());
    }

    @Test
    void testCreateEvent() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Stress Management Workshop"));
    }

    @Test
    void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteEvent() throws Exception {
        // Create first
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventJson))
                .andExpect(status().isCreated());

        // Delete the event
        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRegisterStudentToEvent() throws Exception {
        // Create first
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/events/1/register")
                        .param("studentId", "S123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("registered"));
    }
}
