# Database Seeding via Profile

To balance development convenience with database safety, we decided to restrict database initialization to a specific Spring Boot profile named `bootstrap`. By default, the application runs in the `default` profile and updates the schema (`ddl-auto=update`), but starting with `-Dspring.profiles.active=bootstrap` will trigger a clean drop-create (`ddl-auto=create-drop`) and seed mock users and tasks.
