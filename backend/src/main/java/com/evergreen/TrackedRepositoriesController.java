package com.evergreen;

import java.util.Map;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.evergreen.service.TrackedRepositoryService;
import com.evergreen.model.TrackedRepository;

@RestController 
public class TrackedRepositoriesController {
    // service
    private final TrackedRepositoryService service;

    // data models
    public record TrackRepositoryRequest(String url, String owner, String name) {}

    // constructor
    public TrackedRepositoriesController(TrackedRepositoryService service) {
        this.service = service;
    }

    @PostMapping("/repositories")
    public Map<String, String> trackRepository(@RequestBody TrackRepositoryRequest request) {
        // read fields
        String url = request.url();
        String owner = request.owner();
        String name = request.name();

        // Validate input
        if (
            url == null || url.isBlank() ||
            owner == null || owner.isBlank() ||
            name == null || name.isBlank()
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing one or more of the following fields: url, owner, name.");
        }
        
        service.addTrackedRepository(url, owner, name);

        // success
        return Map.of("success", "true");
    } 

    @GetMapping("/repositories")
    public List<TrackedRepository> getAllTrackedRepositories() {
        return service.getAllTrackedRepositories();
    }
}
