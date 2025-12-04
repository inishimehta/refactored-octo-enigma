package ca.gbc.wellnessresourceservice.controller;

import ca.gbc.wellnessresourceservice.dto.ResourceRequest;
import ca.gbc.wellnessresourceservice.dto.ResourceResponse;
import ca.gbc.wellnessresourceservice.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceResponse create(@Valid @RequestBody ResourceRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<ResourceResponse> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title,asc") String sort
    ) {
        if (category != null && !category.isBlank()) {
            return service.listByCategory(category, page, size, sort);
        } else if (q != null && !q.isBlank()) {
            return service.search(q, page, size, sort);
        } else {
            return service.list(page, size, sort);
        }
    }

    @GetMapping("/{id}")
    public ResourceResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ResourceResponse>> getResourcesByCategory(@PathVariable String category) {
        List<ResourceResponse> resources = service.getResourcesByCategory(category);
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    public ResourceResponse update(@PathVariable Long id, @Valid @RequestBody ResourceRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
