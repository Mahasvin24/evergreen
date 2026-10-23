package com.evergreen;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController 
public class TrackedRepositoriesController {

    public record TrackRepositoryRequest(String url) {}

    @PostMapping("/repositories")
    public Map<String, String> trackRepository(@RequestBody TrackRepositoryRequest request) {
        String url = request.url();

        // invalid URL
        if (url == null || url.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL is required");
        }

        // success
        return Map.of("url", url);
    }   
}
