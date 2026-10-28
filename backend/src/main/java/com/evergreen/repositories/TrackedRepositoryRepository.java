package com.evergreen.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evergreen.model.TrackedRepository;

public interface TrackedRepositoryRepository extends JpaRepository<TrackedRepository, UUID> {}