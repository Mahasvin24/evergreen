import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.Generated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;
import java.time.OffsetDateTime;

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
@EntityListeners(AuditingEntityListener.class) // what is this
public record TrackedRepository(
    @Id @GeneratedValue @UuidGenerator @Column(name = "id", updatable = false, nullable = false) UUID id,
    @Column(name = "owner", updatable = false, nullable = false) String owner,
    @Column(name = "name", updatable = false, nullable = false) String name,
    
    // timestamp
    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false) 
    OffsetDateTime createdAt
) {}