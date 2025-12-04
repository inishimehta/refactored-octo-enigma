package ca.gbc.wellnessresourceservice.config;

import ca.gbc.wellnessresourceservice.model.ResourceEntity;
import ca.gbc.wellnessresourceservice.repository.ResourceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final ResourceRepository repository;

    @PostConstruct
    public void seedResources() {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    new ResourceEntity(null, "Mindfulness Guide", "Learn how to meditate effectively", "mindfulness", "https://example.com/mindfulness"),
                    new ResourceEntity(null, "Fitness Plan", "Weekly beginner gym routine", "fitness", "https://example.com/fitness"),
                    new ResourceEntity(null, "Book Club", "Self-growth reading community", "personal-growth", "https://example.com/books")
            ));
            System.out.println("✅ Seeded initial wellness resources into PostgreSQL");
        }
    }
}
