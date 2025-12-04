package ca.gbc.comp3095.eventservice.controller;

import ca.gbc.comp3095.eventservice.dto.ResourceResponse;
import ca.gbc.comp3095.eventservice.model.Event;
import ca.gbc.comp3095.eventservice.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // ✅ CREATE - 201 Created
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event created = service.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ GET ALL - 200 OK
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = service.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // ✅ GET BY ID - 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        Event event = service.getEventById(id);
        return ResponseEntity.ok(event);
    }

    // ✅ UPDATE - 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event updated = service.updateEvent(id, event);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE - 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ FETCH RESOURCES LINKED BY CATEGORY - 200 OK
    @GetMapping("/resources/{category}")
    public ResponseEntity<List<ResourceResponse>> getResourcesByCategory(@PathVariable String category) {
        List<ResourceResponse> resources = service.getLinkedResources(category);
        return ResponseEntity.ok(resources);
    }

    // ✅ REGISTER STUDENT - 200 OK + JSON STATUS
    @PostMapping("/{id}/register")
    public ResponseEntity<Map<String, String>> registerStudent(@PathVariable Long id,
                                                               @RequestParam String studentId) {
        service.registerStudent(id, studentId);
        return ResponseEntity.ok(Map.of("status", "registered"));
    }

    // ✅ UNREGISTER STUDENT - 200 OK + JSON STATUS
    @PostMapping("/{id}/unregister")
    public ResponseEntity<Map<String, String>> unregisterStudent(@PathVariable Long id,
                                                                 @RequestParam String studentId) {
        service.unregisterStudent(id, studentId);
        return ResponseEntity.ok(Map.of("status", "unregistered"));
    }

    // ✅ GET BY DATE - 200 OK
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Event>> getEventsByDate(@PathVariable String date) {
        List<Event> events = service.findByDate(LocalDate.parse(date));
        return ResponseEntity.ok(events);
    }

    // ✅ GET BY LOCATION - 200 OK
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Event>> getEventsByLocation(@PathVariable String location) {
        List<Event> events = service.findByLocation(location);
        return ResponseEntity.ok(events);
    }
}
