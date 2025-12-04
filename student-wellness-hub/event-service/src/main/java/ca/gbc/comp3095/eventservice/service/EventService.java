package ca.gbc.comp3095.eventservice.service;

import ca.gbc.comp3095.eventservice.dto.ResourceResponse;
import ca.gbc.comp3095.eventservice.model.Event;
import ca.gbc.comp3095.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final WebClient webClient;

    public Event createEvent(Event event) {
        return repository.save(event);
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public Event getEventById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }
    public List<ResourceResponse> getLinkedResources(String category) {
        return webClient.get()
                .uri("http://wellness-resource-service:8091/api/resources?category={category}", category)
                .retrieve()
                .bodyToFlux(ResourceResponse.class)
                .collectList()
                .block();
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);
        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setDate(updatedEvent.getDate());
        existing.setLocation(updatedEvent.getLocation());
        existing.setCapacity(updatedEvent.getCapacity());
        return repository.save(existing);
    }

    public Event registerStudent(Long id, String studentId) {
        Event event = getEventById(id);
        List<String> students = event.getRegisteredStudents();
        if (students.size() < event.getCapacity() && !students.contains(studentId)) {
            students.add(studentId);
        }
        event.setRegisteredStudents(students);
        return repository.save(event);
    }

    public Event unregisterStudent(Long id, String studentId) {
        Event event = getEventById(id);
        event.getRegisteredStudents().remove(studentId);
        return repository.save(event);
    }

    public List<Event> findByDate(java.time.LocalDate date) {
        return repository.findByDate(date);
    }

    public List<Event> findByLocation(String location) {
        return repository.findByLocationContainingIgnoreCase(location);
    }
}
