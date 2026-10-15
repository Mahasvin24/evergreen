CREATE TABLE tracked_repositories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                   -- random id
    owner TEXT NOT NULL,                                             -- GitHub user or organization that owns the repository
    name TEXT NOT NULL,                                              -- name of repository
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    CONSTRAINT unique_repository UNIQUE (owner, name)                -- avoids duplicate repo registration
);
