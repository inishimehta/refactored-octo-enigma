package ca.gbc.goaltrackingservice.controller;

import ca.gbc.goaltrackingservice.dto.GoalRequest;
import ca.gbc.goaltrackingservice.dto.GoalResponse;
import ca.gbc.goaltrackingservice.service.GoalService;
import ca.gbc.goaltrackingservice.dto.ResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    // ✅ POST — Create goal → 201 Created
    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@RequestBody GoalRequest request) {
        GoalResponse createdGoal = goalService.createGoal(request);
        URI location = URI.create("/api/goals/" + createdGoal.goalId());
        return ResponseEntity.created(location).body(createdGoal);
    }

    // ✅ GET — Retrieve all → 200 OK
    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAllGoals() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }

    // ✅ GET — Filter by category → 200 OK
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GoalResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(goalService.getGoalsByCategory(category));
    }

    @GetMapping("/suggest/{category}")
    public ResponseEntity<List<ResourceResponse>> suggestResources(@PathVariable String category) {
        List<ResourceResponse> resources = goalService.suggestResources(category);
        return ResponseEntity.ok(resources);
    }


    // ✅ GET — Filter by status → 200 OK
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoalResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(goalService.getGoalsByStatus(status));
    }

    // ✅ PUT — Update goal → 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(@PathVariable String id, @RequestBody GoalRequest request) {
        return ResponseEntity.ok(goalService.updateGoal(id, request));
    }

    // ✅ PUT — Mark as completed → 200 OK
    @PutMapping("/{id}/complete")
    public ResponseEntity<GoalResponse> markCompleted(@PathVariable String id) {
        return ResponseEntity.ok(goalService.markCompleted(id));
    }

    // ✅ DELETE — Delete goal → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable String id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
