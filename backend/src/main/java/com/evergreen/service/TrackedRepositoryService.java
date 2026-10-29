package com.evergreen.service;

import org.springframework.stereotype.Service;

import com.evergreen.model.TrackedRepository;
import com.evergreen.repositories.TrackedRepositoryRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;;

@Service 
public class TrackedRepositoryService {

    // store repo associated with this service
    private final TrackedRepositoryRepository repository;

    // constructor
    public TrackedRepositoryService(TrackedRepositoryRepository repository) {
        this.repository = repository;
    }

    public List<TrackedRepository> getAllTrackedRepositories() {
        return repository.findAll();
    }

    public Optional<TrackedRepository> getByID(UUID id) {
        return repository.findById(id);
    }

    public void addTrackedRepository(String owner, String name) {
        TrackedRepository obj = new TrackedRepository(owner, name);
        repository.save(obj);
    }
}