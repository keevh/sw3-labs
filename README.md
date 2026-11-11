# Sistema Academico

Proyecto de Ingeniería de Software III. API REST del sistema académico con Spring Boot y H2 en memoria. Cada laboratorio está en su propia rama (`lab-01`, `lab-02`, `lab-03`, `lab-04`).

En esta rama está el Lab 04. Hay un proyecto base de estudiantes por capas y dos microservicios: `auth-service`, que hace el login y genera el JWT, y `consulta-service`, que expone los estudiantes y solo deja consultarlos con un token válido.

## Qué agrega cada laboratorio

### Lab 04
- Proyecto base `demo-academico` hecho desde cero con capas controller → handler → service → repository → model. El estudiante ahora tiene nombre, apellido, email y edad.
- `auth-service` con usuarios en H2, contraseñas cifradas con BCrypt y tokens JWT.
- `consulta-service`, que es una copia del proyecto base. Valida el token con `auth-service` usando OpenFeign y un filtro de seguridad.
- Swagger con el botón Authorize (esquema Bearer).
- Dockerfile para cada servicio y `docker-compose.yml`.

### Lab 03 (rama `lab-03`)
- Programas, asignaturas, grupos y matrículas, con reglas de cupo y sin matrículas duplicadas.
- Código organizado por módulos, con los Ports `EstudianteQueryPort` y `GrupoQueryPort`.

### Lab 02 (rama `lab-02`)
- Paginación, Swagger UI, excepciones propias, `GlobalExceptionHandler` y seeder con JavaFaker.

### Lab 01 (rama `lab-01`)
- Módulo de estudiantes por capas con H2.

## Estructura

```
sw3-labs/
├── demo-academico/                  proyecto base (puerto 8080)
├── microservicios-demoacademico/
│   ├── auth-service/                login y JWT (puerto 8081)
│   ├── consulta-service/            estudiantes protegidos (puerto 8082)
│   └── docker-compose.yml
└── informes/
```

## Tecnologías

- Java 21 y Spring Boot 3.2.5
- Spring Data JPA, Bean Validation y H2 en memoria
- Spring Security con BCrypt y JWT (jjwt 0.11.5)
- Spring Cloud OpenFeign 2023.0.1
- Springdoc 2.6.0 (Swagger UI)
- JavaFaker
- Docker y Docker Compose

## Cómo ejecutar

### Local

Cada proyecto tiene su Maven Wrapper. Primero se levanta `auth-service` y después `consulta-service`, en terminales separadas:

```bash
cd microservicios-demoacademico/auth-service
sh ./mvnw spring-boot:run
```

```bash
cd microservicios-demoacademico/consulta-service
sh ./mvnw spring-boot:run
```

El proyecto base se corre igual desde `demo-academico/` y queda en el puerto 8080. En Windows se usa `mvnw.cmd spring-boot:run`.

### Con Docker Compose

Los Dockerfile copian el JAR ya compilado, así que primero hay que empaquetar los dos servicios:

```bash
cd microservicios-demoacademico
(cd auth-service && sh ./mvnw clean package -DskipTests)
(cd consulta-service && sh ./mvnw clean package -DskipTests)
docker compose up --build
```

Dentro de Docker, `consulta-service` llama a `auth-service` en `http://auth-service:8081`. Ese valor llega por la variable `SERVICES_AUTH_URL` del compose. Para bajar todo se usa `docker compose down`.

En el compose quitamos la línea `version: '3.9'` que trae la guía, porque Docker Compose ya no la usa y muestra un aviso.

## URLs

| Servicio | Swagger | H2 Console (JDBC URL) |
|----------|---------|-----------------------|
| auth-service | http://localhost:8081/swagger-ui.html | http://localhost:8081/h2-console (`jdbc:h2:mem:authdb`) |
| consulta-service | http://localhost:8082/swagger-ui.html | http://localhost:8082/h2-console (`jdbc:h2:mem:demoacademico`) |
| demo-academico | http://localhost:8080/swagger-ui.html | http://localhost:8080/h2-console (`jdbc:h2:mem:demoacademico`) |

En H2 el usuario es `sa` y la contraseña va vacía.

## Usuarios de prueba

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | Admin2026* | ADMIN |
| docente | Docente2026* | DOCENTE |
| estudiante1 | Estu2026* | ESTUDIANTE |

## Cómo usar Authorize

1. En el Swagger de `auth-service`, ejecutar `POST /auth/login` con un usuario de la tabla.
2. Copiar solo el valor del campo `token`.
3. En el Swagger de `consulta-service`, hacer clic en **Authorize**, pegar el token **sin** escribir `Bearer` y confirmar. Swagger agrega el prefijo solo.
4. Ejecutar `GET /api/estudiantes`.

Mensajes de error del filtro:
- `Token no enviado o formato inválido`: no se cargó el token en Authorize.
- `Token inválido o expirado`: el token está mal copiado, alterado o ya venció (dura 1 hora).

`POST /api/estudiantes` queda público porque el filtro de la guía lo excluye. El resto de rutas de `/api/estudiantes` pide token.
