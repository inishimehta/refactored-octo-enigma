package ca.gbc.wellnessresourceservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resources", indexes = {
        @Index(name = "idx_resources_category", columnList = "category"),
        @Index(name = "idx_resources_title", columnList = "title")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 80)
    private String category; // e.g., counseling, mindfulness

    @Column(nullable = false, length = 512)
    private String url;
}
