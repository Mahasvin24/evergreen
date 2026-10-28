package com.evergreen.service;

import org.springframework.stereotype.Service;

import com.evergreen.model.TrackedRepository;

@Service 
public class TrackedRepositoryService {

    // store repo associated with this service
    private final TrackedRepository repo;

    // constructor
    public TrackedRepositoryService(TrackedRepository repo) {
        this.repo = repo;
    }

    

}