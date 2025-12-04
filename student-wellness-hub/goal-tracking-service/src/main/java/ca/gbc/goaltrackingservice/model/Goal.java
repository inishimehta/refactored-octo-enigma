package ca.gbc.goaltrackingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "goals")
public class Goal {

    @Id
    private String goalId;

    private String title;
    private String description;
    private LocalDate targetDate;
    private String status;      // "in-progress", "completed"
    private String category;    // e.g. "mindfulness", "fitness"
}
