package ca.gbc.goaltrackingservice.service;

import ca.gbc.goaltrackingservice.dto.GoalRequest;
import ca.gbc.goaltrackingservice.dto.GoalResponse;
import ca.gbc.goaltrackingservice.dto.ResourceResponse;
import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.repository.GoalRepository;
import ca.gbc.goaltrackingservice.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final WebClient webClient;

    // Create a new goal
    public GoalResponse createGoal(GoalRequest request) {
        Goal goal = Goal.builder()
                .title(request.title())
                .description(request.description())
                .targetDate(request.targetDate())
                .status(request.status())
                .category(request.category())
                .build();

        Goal saved = goalRepository.save(goal);
        return mapToResponse(saved);
    }

    // Retrieve all goals
    public List<GoalResponse> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Retrieve by category
    public List<GoalResponse> getGoalsByCategory(String category) {
        return goalRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ResourceResponse> suggestResources(String category) {
        return webClient.get()
                .uri("http://wellness-resource-service:8091/api/resources?category={category}", category)
                .retrieve()
                .bodyToFlux(ResourceResponse.class)
                .collectList()
                .block();
    }
    // Retrieve by status
    public List<GoalResponse> getGoalsByStatus(String status) {
        return goalRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update existing goal
    public GoalResponse updateGoal(String id, GoalRequest request) {
        Goal existing = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found: " + id));

        existing.setTitle(request.title());
        existing.setDescription(request.description());
        existing.setTargetDate(request.targetDate());
        existing.setStatus(request.status());
        existing.setCategory(request.category());

        Goal updated = goalRepository.save(existing);
        return mapToResponse(updated);
    }

    // Delete a goal
    public void deleteGoal(String id) {
        if (!goalRepository.existsById(id)) {
            throw new RuntimeException("Goal not found: " + id);
        }
        goalRepository.deleteById(id);
    }

    // Mark a goal as completed
    public GoalResponse markCompleted(String id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found: " + id));

        goal.setStatus("completed");
        Goal completed = goalRepository.save(goal);

        return mapToResponse(completed);
    }

    // Helper method: maps Goal entity -> GoalResponse DTO
    private GoalResponse mapToResponse(Goal goal) {
        return new GoalResponse(
                goal.getGoalId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getTargetDate(),
                goal.getStatus(),
                goal.getCategory()
        );
    }
}
