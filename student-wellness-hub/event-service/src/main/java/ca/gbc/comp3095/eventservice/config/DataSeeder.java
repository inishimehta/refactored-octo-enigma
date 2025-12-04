package ca.gbc.comp3095.eventservice.config;

import ca.gbc.comp3095.eventservice.model.Event;
import ca.gbc.comp3095.eventservice.repository.EventRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final EventRepository repository;

    @PostConstruct
    public void seedEvents() {
        if (repository.count() == 0) {
            List<Event> events = new ArrayList<>();

            events.add(Event.builder()
                    .title("Mindfulness Workshop")
                    .description("A session on managing stress and practicing mindfulness.")
                    .date(LocalDate.of(2025, 11, 20))
                    .location("Room 101")
                    .capacity(30)
                    .registeredStudents(new ArrayList<>())
                    .build());

            events.add(Event.builder()
                    .title("Campus Fitness Challenge")
                    .description("Join the campus-wide fitness competition.")
                    .date(LocalDate.of(2025, 11, 25))
                    .location("Gymnasium")
                    .capacity(50)
                    .registeredStudents(new ArrayList<>())
                    .build());

            events.add(Event.builder()
                    .title("Personal Growth Seminar")
                    .description("Learn techniques for self-improvement and motivation.")
                    .date(LocalDate.of(2025, 12, 2))
                    .location("Auditorium A")
                    .capacity(100)
                    .registeredStudents(new ArrayList<>())
                    .build());

            repository.saveAll(events);
            System.out.println("✅ Seeded initial events into PostgreSQL");
        }
    }
}
