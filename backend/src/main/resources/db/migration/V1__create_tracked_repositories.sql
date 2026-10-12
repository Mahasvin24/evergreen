CREATE TABLE tracked_repositories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                   -- random id
    owner TEXT NOT NULL,                                             -- reference to the user's github acc
    name TEXT NOT NULL,                                              -- name of repostiory
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    CONSTRAINT unique_repo UNIQUE (owner, name)                      -- avoids duplicate repo registration
)
