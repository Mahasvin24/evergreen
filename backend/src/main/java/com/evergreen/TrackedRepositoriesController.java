package com.evergreen;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.evergreen.service.TrackedRepositoryService;

@RestController 
public class TrackedRepositoriesController {
    // servuce
    public final TrackedRepositoryService service;

    // data models
    public record TrackRepositoryRequest(String url, String owner, String name) {}

    // constructor
    public TrackedRepositoriesController(TrackedRepositoryService service) {
        this.service = service;
    }

    @PostMapping("/repositories")
    public Map<String, String> trackRepository(@RequestBody TrackRepositoryRequest request) {
        String url = request.url();
        String owner = request.owner();
        String name = request.name();

        // Validate input
        if (
            url == null || url.isBlank() ||
            owner == null || owner.isBlank() ||
            name == null || name.isBlank()
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL, owner, and name are required fields.");
        }

        // success
        return Map.of("url", url);
    } 
}
