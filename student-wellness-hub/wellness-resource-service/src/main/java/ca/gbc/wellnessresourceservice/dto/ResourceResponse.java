package ca.gbc.wellnessresourceservice.dto;

import lombok.*;

@Value
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class ResourceResponse {
    Long resourceId;
    String title;
    String description;
    String category;
    String url;
}
