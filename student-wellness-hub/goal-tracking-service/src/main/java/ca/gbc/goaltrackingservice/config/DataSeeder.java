package ca.gbc.goaltrackingservice.config;

import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.repository.GoalRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final GoalRepository goalRepository;

    @PostConstruct
    public void seedGoals() {
        if (goalRepository.count() == 0) {
            goalRepository.saveAll(List.of(
                    new Goal(null, "Meditate Daily", "Spend 10 minutes meditating", LocalDate.of(2025, 12, 31), "in-progress", "mindfulness"),
                    new Goal(null, "Exercise Weekly", "Go to the gym twice a week", LocalDate.of(2025, 11, 30), "in-progress", "fitness"),
                    new Goal(null, "Read Books", "Read 2 self-improvement books per month", LocalDate.of(2025, 12, 15), "in-progress", "personal-growth")
            ));
            System.out.println("✅ Seeded initial goals into MongoDB");
        }
    }
}
