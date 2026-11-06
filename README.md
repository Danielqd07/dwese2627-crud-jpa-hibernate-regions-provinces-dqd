# CRUD de Regiones y Provincias

Aplicación web desarrollada con **Spring Boot** para gestionar Comunidades Autónomas y Provincias mediante operaciones CRUD.

El proyecto utiliza **JPA/Hibernate** para trabajar con la base de datos y también incorpora **DTOs y Mappers** para separar las entidades de la información que se muestra en las vistas.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- JPA / Hibernate
- Thymeleaf
- MySQL
- Maven
- Bootstrap
- Lombok
- Jakarta Validation

## Funcionalidades

La aplicación permite:

- Crear, editar, eliminar y consultar Regiones.
- Crear, editar, eliminar y consultar Provincias.
- Ver el detalle de cada registro.
- Relacionar cada Provincia con su Región correspondiente.
- Validar los datos introducidos en los formularios.
- Evitar códigos duplicados.
- Paginar los listados.
- Ordenar los registros por ID, código o nombre.
- Mostrar la aplicación en español e inglés mediante internacionalización.

## Estructura del proyecto

El proyecto está organizado principalmente en:

- `entities`: entidades JPA.
- `daos`: acceso a la base de datos.
- `dtos`: objetos utilizados para transportar los datos.
- `mappers`: conversión entre entidades y DTOs.
- `controllers`: lógica de las rutas y vistas.
- `templates`: vistas realizadas con Thymeleaf.

## Ejecución

Para compilar el proyecto:

```bash
./mvnw clean compile
```

Para ejecutarlo:

```bash
./mvnw spring-boot:run
```

Después se puede acceder desde:

```text
http://localhost:8080
```

## Autor

**Daniel Quintero Díaz**  
2º Desarrollo de Aplicaciones Web - IES Alixar
