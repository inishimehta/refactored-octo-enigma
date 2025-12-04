package ca.gbc.wellnessresourceservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResourceRequest {
    @NotBlank @Size(max = 160)
    private String title;

    @NotBlank @Size(max = 2000)
    private String description;

    @NotBlank @Size(max = 80)
    private String category;

    @NotBlank @Size(max = 512)
    private String url;
}
