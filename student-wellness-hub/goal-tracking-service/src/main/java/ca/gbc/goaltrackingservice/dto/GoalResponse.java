package ca.gbc.goaltrackingservice.dto;

import java.time.LocalDate;

public record GoalResponse(
        String goalId,
        String title,
        String description,
        LocalDate targetDate,
        String status,
        String category
) {}
