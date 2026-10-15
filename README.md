# Evergreen

An autonomous software maintainer.

## Local development

Requires Java 21 and Docker Desktop. Maven is provided by `backend/mvnw`.

Create a root `.env` file from `.env.example` if you do not already have one,
then set `POSTGRES_PASSWORD`. Keep this file out of Git. If the PostgreSQL
volume is already initialized, use its existing password; editing `.env`
does not change the password stored in PostgreSQL.

From the repository root:

```sh
docker compose up -d postgres
set -a
source .env
set +a
cd backend
./mvnw spring-boot:run
```

Compose reads `.env` automatically. The `source` step exports its values for
the separately launched backend. The default JDBC URL points to the Compose
database, `everygreen` (including the extra `y`), and the default database user
is `mahasvin`. Override these using `DATABASE_URL` (a JDBC URL) and
`POSTGRES_USER` when connecting to a different database.

Spring Boot runs Flyway at startup using SQL files in
`backend/src/main/resources/db/migration`. V1 creates `tracked_repositories`;
Flyway records successful migrations in `flyway_schema_history` and skips
them on subsequent starts. This setup uses `application.properties` and
does not require a separate `flyway.conf` or Flyway CLI.

Check the API at `http://localhost:8080/health`. From another terminal at the
repository root, inspect the migration history:

```sh
docker compose exec postgres psql -U mahasvin -d everygreen \
  -c 'SELECT version, description, success FROM flyway_schema_history;'
```

To run backend tests, keep PostgreSQL running and run `./mvnw test` from
`backend/` in a shell with `POSTGRES_PASSWORD` exported. The context test
connects to that database and applies pending migrations, just like startup.

Once a migration has been applied, leave it unchanged and add the next
version (for example, `V2__add_repository_settings.sql`) for schema changes.
