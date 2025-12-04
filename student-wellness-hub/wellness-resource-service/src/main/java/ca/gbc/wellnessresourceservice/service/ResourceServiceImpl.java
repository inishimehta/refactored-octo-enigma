package ca.gbc.wellnessresourceservice.service;

import ca.gbc.wellnessresourceservice.config.RedisConfig;
import ca.gbc.wellnessresourceservice.dto.ResourceRequest;
import ca.gbc.wellnessresourceservice.dto.ResourceResponse;
import ca.gbc.wellnessresourceservice.model.ResourceEntity;
import ca.gbc.wellnessresourceservice.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;

    private ResourceResponse toResponse(ResourceEntity e) {
        return ResourceResponse.builder()
                .resourceId(e.getResourceId())
                .title(e.getTitle())
                .description(e.getDescription())
                .category(e.getCategory())
                .url(e.getUrl())
                .build();
    }

    private ResourceEntity fromRequest(ResourceRequest r) {
        return ResourceEntity.builder()
                .title(r.getTitle())
                .description(r.getDescription())
                .category(r.getCategory())
                .url(r.getUrl())
                .build();
    }

    private Pageable pageable(int page, int size, String sort) {
        if (sort == null || sort.isBlank()) return PageRequest.of(page, size);
        String[] parts = sort.split(",");
        String prop = parts[0];
        Sort.Direction dir = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, prop));
    }

    @Override
    @CacheEvict(value = {RedisConfig.CACHE_RESOURCES, RedisConfig.CACHE_RESOURCES_BY_CATEGORY}, allEntries = true)
    public ResourceResponse create(ResourceRequest req) {
        ResourceEntity saved = repository.save(fromRequest(req));
        return toResponse(saved);
    }

    @Override
    @CacheEvict(value = {RedisConfig.CACHE_RESOURCES, RedisConfig.CACHE_RESOURCES_BY_CATEGORY}, allEntries = true)
    @CachePut(cacheNames = RedisConfig.CACHE_RESOURCE_BY_ID, key = "#id")
    public ResourceResponse update(Long id, ResourceRequest req) {
        ResourceEntity e = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setCategory(req.getCategory());
        e.setUrl(req.getUrl());
        return toResponse(repository.save(e));
    }

    @Override
    @CacheEvict(value = {RedisConfig.CACHE_RESOURCES, RedisConfig.CACHE_RESOURCES_BY_CATEGORY}, allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Resource not found");
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = RedisConfig.CACHE_RESOURCE_BY_ID, key = "#id")
    public ResourceResponse getById(Long id) {
        ResourceEntity e = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
        return toResponse(e);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = RedisConfig.CACHE_RESOURCES, key = "#page+'-'+#size+'-'+#sort")
    public List<ResourceResponse> list(int page, int size, String sort) {
        Page<ResourceEntity> p = repository.findAll(pageable(page, size, sort));
        return p.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = RedisConfig.CACHE_RESOURCES_BY_CATEGORY, key = "#category+'-'+#page+'-'+#size+'-'+#sort")
    public List<ResourceResponse> listByCategory(String category, int page, int size, String sort) {
        Page<ResourceEntity> p = repository.findByCategoryIgnoreCase(category, pageable(page, size, sort));
        return p.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> search(String q, int page, int size, String sort) {
        Page<ResourceEntity> p = repository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q, pageable(page, size, sort));
        return p.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = RedisConfig.CACHE_RESOURCES_BY_CATEGORY, key = "#category")
    public List<ResourceResponse> getResourcesByCategory(String category) {
        // Reuse your existing pageable() helper for default pagination and sorting
        Pageable pageable = pageable(0, 20, "title,asc");

        Page<ResourceEntity> page = repository.findByCategoryIgnoreCase(category, pageable);

        return page.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
