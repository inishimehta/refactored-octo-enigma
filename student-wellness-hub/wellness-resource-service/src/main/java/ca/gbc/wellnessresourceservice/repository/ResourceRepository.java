package ca.gbc.wellnessresourceservice.repository;

import ca.gbc.wellnessresourceservice.model.ResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    Page<ResourceEntity> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<ResourceEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titlePart, String descPart, Pageable pageable);
}
