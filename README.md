# Sistema Academico

Proyecto de Ingeniería de Software III. API REST del sistema académico con Spring Boot y base de datos H2 en memoria. Empezó como el módulo de estudiantes por capas (Lab 01) y ahora incluye programas, asignaturas, grupos y matrículas, organizados por módulos.

## Tecnologías

- Java 21
- Spring Boot 3.2.4
- Spring Data JPA
- Bean Validation
- H2 (en memoria)
- Springdoc (Swagger UI)
- JavaFaker (datos de prueba)
- Maven

## Estructura

```
co.edu.demoacademico
├── common        (ApiResponse, excepciones comunes y GlobalExceptionHandler)
├── config        (seeders)
├── estudiantes   (model, dto, repository, service, handler, controller, port)
├── programas     (model, dto, repository, service, handler, controller)
├── asignaturas   (model, dto, repository, service, handler, controller)
├── grupos        (model, dto, repository, service, handler, controller, port)
└── matriculas    (model, dto, repository, service, handler, controller)
```

Matrículas no usa los repositorios de otros módulos. Consulta estudiantes y grupos solo a través de dos Ports:

| Port | Módulo | Lo implementa |
|------|--------|---------------|
| EstudianteQueryPort | estudiantes | EstudianteServiceImpl |
| GrupoQueryPort | grupos | GrupoServiceImpl |

## Endpoints

| Recurso | Endpoints |
|---------|-----------|
| Estudiantes | POST y GET /api/estudiantes, GET /api/estudiantes/pagina, GET /api/estudiantes/buscar?email=, GET, PUT y DELETE /api/estudiantes/{id} |
| Programas | POST y GET (paginado) /api/programas, GET, PUT y DELETE /api/programas/{id} |
| Asignaturas | POST y GET /api/asignaturas, DELETE /api/asignaturas/{id} |
| Grupos | POST y GET (paginado) /api/grupos, DELETE /api/grupos/{id} |
| Matrículas | POST /api/matriculas |

Las respuestas vienen con el formato `ApiResponse`: `success`, `message`, `data` y `timestamp`.

## Reglas de matrícula

- Si el estudiante o el grupo no existen responde 404.
- Si el estudiante ya está matriculado en ese grupo responde 409.
- Si el grupo ya llegó a su cupo máximo responde 409.

## Cómo ejecutar

```bash
sh ./mvnw spring-boot:run
```

La aplicación corre en `http://localhost:8080`.

Swagger UI está en `http://localhost:8080/swagger-ui.html`.

La consola H2 está disponible en `http://localhost:8080/h2-console` con la URL `jdbc:h2:mem:demoacademico`, usuario `sa` y contraseña vacía.

Al iniciar se cargan datos de prueba: 50 estudiantes, 3 programas, 10 asignaturas y 20 grupos. Las cantidades se cambian en `app.seed` dentro de `application.yml`. No se crean matrículas.
