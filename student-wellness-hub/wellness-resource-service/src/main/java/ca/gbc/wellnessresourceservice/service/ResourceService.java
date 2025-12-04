package ca.gbc.wellnessresourceservice.service;

import ca.gbc.wellnessresourceservice.dto.ResourceRequest;
import ca.gbc.wellnessresourceservice.dto.ResourceResponse;

import java.util.List;

public interface ResourceService {
    ResourceResponse create(ResourceRequest req);
    List<ResourceResponse> list(int page, int size, String sort);
    List<ResourceResponse> listByCategory(String category, int page, int size, String sort);
    List<ResourceResponse> search(String query, int page, int size, String sort);
    ResourceResponse getById(Long id);
    ResourceResponse update(Long id, ResourceRequest req);
    void delete(Long id);
    long count(); // total number of resources

    List<ResourceResponse> getResourcesByCategory(String category);
}
