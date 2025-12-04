package ca.gbc.comp3095.eventservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceResponse {
    private String id;
    private String title;
    private String description;
    private String category;
    private String link;
}
