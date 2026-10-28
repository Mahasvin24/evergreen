package com.evergreen.model;

import java.util.UUID;
import java.time.Instant;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// CREATE TABLE tracked_repositories (
//     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                   -- random id
//     owner TEXT NOT NULL,                                             -- GitHub user or organization that owns the repository
//     name TEXT NOT NULL,                                              -- name of repository
//     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
//     CONSTRAINT unique_repository UNIQUE (owner, name)                -- avoids duplicate repo registration
// );

@Entity
@Table(
    name="tracked_repositories",
    uniqueConstraints = @UniqueConstraint(name="unique_repository", columnNames = {"owner", "name"})
)
@EntityListeners(AuditingEntityListener.class)
public class TrackedRepository {
    /* Data */
    @Id @GeneratedValue @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "owner", updatable = false, nullable = false)
    private String owner;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
    private Instant createdAt;

    /* Constructor */
    protected TrackedRepository() {}
    public TrackedRepository(String owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    /* Functions */
    public UUID getId() {
        return this.id;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }
}
