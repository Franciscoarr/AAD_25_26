# Sistema de Gestión de Matrículas (JDBC + PostgreSQL + Docker)

## 1. Descripción General

Este proyecto implementa un **sistema de gestión de matrículas** utilizando **JDBC puro**, conectado a una base de datos **PostgreSQL desplegada en Docker**.

El objetivo principal de la práctica es comprender:

- Cómo conectar una aplicación Java con PostgreSQL mediante JDBC.
- Cómo realizar operaciones CRUD sobre entidades como **Alumno**, **Módulo** y **Matrícula**.
- Cómo gestionar **transacciones manuales** (BEGIN, COMMIT, ROLLBACK).
- Cómo invocar **funciones almacenadas** desde Java utilizando `CallableStatement`.
- Cómo trabajar con consultas **parametrizadas** para mayor seguridad.
- Cómo ejecutar el proyecto desde **Maven** con Spring Boot

El proyecto se organiza con una estructura de repositorios (CRUD), servicios y una clase principal con un método `run()` para validar todo el flujo del sistema.

---

## 2. Instrucciones de Ejecución

Sigue los pasos indicados para levantar la base de datos, compilar y ejecutar el proyecto.

### 2.1 Levantar PostgreSQL con Docker

Tendremos que tener Docker instalado en nuestra máquina. Luego, en nuestro proyecto crearemos un archivo llamado `docker-compose.yml` con el siguiente contenido:

```yaml
services:
  db:
    image: postgres:latest
    container_name: aad_db_container
    restart: always
    environment:
      POSTGRES_DB: aad_db
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
    ports:
      - "5433:5432"
    volumes:
      - db_data:/var/lib/postgresql

volumes:
  db_data:
```
Luego, desde la terminal, navegamos hasta el directorio donde se encuentra el archivo `docker-compose.yml` y ejecutamos:

```bash
docker-compose up -d
```

Esto levantará un contenedor con PostgreSQL accesible en el puerto `5433`

### 2.2 Compilar y ejecutar el proyecto con Maven

Asegúrate de tener Maven instalado, nuestro proyecto tendrá un archivo `pom.xml` configurado para gestionar las dependencias necesarias la cual deberá incluir lo siguiente:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.5</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.farrnav3006</groupId>
    <artifactId>aad</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>aad</name>
    <description>Acceso a datos</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Dependencia en pom.xml para incluir el driver PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.7</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
Una vez configurado el `pom.xml`, podemos compilar y ejecutar el proyecto.

### 2.3 Ejecutar el método run()
Para ejecutar el método `run()`, debemos tener este código en nuestra clase principal:

```java
package com.farrnav3006.aad;

import com.farrnav3006.aad.application.StudentManagementService;
import com.farrnav3006.aad.config.PostgresqlDriver;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final PostgresqlDriver postgresqlDriver;
    private final StudentManagementService studentManagementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student miriam = new Student(null, "66280457T", "Miriam", "miriam@g.educaand.es");
        Module programacion = new Module(null, "0485", "Programación", 250);

        miriam = studentManagementService.createStudent(miriam);
        programacion = studentManagementService.createModule(programacion);

        int modulosMatriculados = studentManagementService.countEnrollments(miriam.getId());
        log.info("{} módulos matriculados para el alumno {}", modulosMatriculados, miriam.getName());

        studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());
        studentRepository.delete(miriam.getId());
    }
}
```
Como se puede observar, el método `run()` proporcionado crea un alumno y un módulo, los matricula y luego elimina al alumno.

## 3. Evidencias de ejecución

A continuación, se muestran capturas de la terminal que evidencian la correcta ejecución del proyecto:

```bash
Powered by AAD 3.5.5
2025-11-30T10:57:46.662+01:00  INFO 27264 --- [           main] com.farrnav3006.aad.Application          : Starting Application using Java 25 with PID 27264 (C:\Users\PepsiPaco\IdeaProjects\AAD_25_26\target\classes started by PepsiPaco in C:\Users\PepsiPaco\IdeaProjects\AAD_25_26)
2025-11-30T10:57:46.664+01:00  INFO 27264 --- [           main] com.farrnav3006.aad.Application          : No active profile set, falling back to 1 default profile: "default"
2025-11-30T10:57:47.611+01:00  INFO 27264 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-11-30T10:57:47.625+01:00  INFO 27264 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-11-30T10:57:47.625+01:00  INFO 27264 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.44]
2025-11-30T10:57:47.667+01:00  INFO 27264 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-11-30T10:57:47.668+01:00  INFO 27264 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 954 ms
2025-11-30T10:57:47.728+01:00  INFO 27264 --- [           main] c.f.aad.config.PostgresqlDriver          : Initializing database...
2025-11-30T10:57:47.911+01:00  INFO 27264 --- [           main] c.f.aad.config.PostgresqlDriver          : Executed script: class org.springframework.core.io.FileSystemResource
2025-11-30T10:57:47.942+01:00  INFO 27264 --- [           main] c.f.aad.config.PostgresqlDriver          : Executed script: class org.springframework.core.io.FileSystemResource
2025-11-30T10:57:47.942+01:00  INFO 27264 --- [           main] c.f.aad.config.PostgresqlDriver          : Database initialized successfully!
2025-11-30T10:57:48.251+01:00  INFO 27264 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-11-30T10:57:48.257+01:00  INFO 27264 --- [           main] com.farrnav3006.aad.Application          : Started Application in 2.144 seconds (process running for 2.566)
2025-11-30T10:57:48.288+01:00  INFO 27264 --- [           main] c.f.aad.repository.StudentRepository     : create OK: Student(id=1, nif=66280457T, name=Miriam, email=miriam@g.educaand.es)
2025-11-30T10:57:48.305+01:00  INFO 27264 --- [           main] c.f.aad.repository.ModuleRepository      : create OK: Module(id=1, code=0485, name=Programación, hours=250)
2025-11-30T10:57:48.324+01:00  INFO 27264 --- [           main] com.farrnav3006.aad.Application          : 1 módulos matriculados para el alumno Miriam
2025-11-30T10:57:48.337+01:00  INFO 27264 --- [           main] c.f.aad.repository.StudentRepository     : findById OK: Student(id=1, nif=66280457T, name=Miriam, email=miriam@g.educaand.es)
2025-11-30T10:57:48.338+01:00  INFO 27264 --- [           main] c.f.aad.repository.ModuleRepository      : findById OK: Module(id=1, code=0485, name=Programación, hours=250)
2025-11-30T10:57:48.340+01:00  INFO 27264 --- [           main] c.f.aad.repository.EnrollmentRepository  : create OK: Enrollment(studentId=1, moduleId=1, date=2025-11-30)
2025-11-30T10:57:48.356+01:00  INFO 27264 --- [           main] c.f.aad.repository.StudentRepository     : delete OK for id=1
```

Estas evidencias muestran que el sistema se ha inicializado correctamente, se han creado las entidades necesarias, se ha realizado la matrícula y finalmente se ha eliminado al alumno.

## 4. Conclusión personal

En esta práctica he aprendido cómo trabajar con JDBC, gestionando manualmente las conexiones, consultas y resultados. He podido implementar operaciones CRUD completas utilizando Connection, PreparedStatement y ResultSet. También he trabajado con transacciones manuales, lo que me ha permitido ver la importancia del control en operaciones críticas, además de aplicar consultas parametrizadas para mejorar la seguridad frente a inyecciones SQL. Pude ejecutar funciones almacenadas mediante CallableStatement y comprobar cómo se integran sin problemas dentro del flujo de la aplicación. Finalmente, montar PostgreSQL en Docker me ha ayudado a disponer de un entorno limpio, reproducible y muy fácil de levantar.