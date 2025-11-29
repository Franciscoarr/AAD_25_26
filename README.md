Descripción
Este proyecto demuestra la integración entre una aplicación Spring Boot y una base de datos PostgreSQL utilizando Docker. El sistema gestiona estudiantes, módulos y matriculaciones.

Paso a Paso de Ejecución
1. Levantar PostgreSQL con Docker
   Ejecuta el siguiente comando desde el directorio del proyecto donde se encuentra el archivo docker-compose.yml: docker-compose up -d

Verificación: Comprueba que el contenedor esté ejecutándose: docker ps
Deberías ver el contenedor aad_db_container en ejecución.

2. Compilar y Ejecutar el Proyecto con Maven
   
Debemos introducir lo siguiente en el archivo pom.xml para tener la dependencia de PostgreSQL y una vez puesto, tendremos que darle al icono de la M para recargar las dependecias:

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.7</version>
        </dependency>

3. Ejecutar el Método run() para Validar el Sistema
   El método run() se ejecuta automáticamente al iniciar la aplicación y realiza las siguientes acciones:

- Crea registros en las tablas student, module y enrollment
- Muestra trazas en consola del proceso
- Guarda los registros en la base de datos PostgreSQL

Salida esperada en consola:

✅ Base de datos PostgreSQL conectada correctamente

✅ Tablas creadas/verificadas correctamente

📝 Insertando estudiantes de ejemplo...

📝 Insertando módulos de ejemplo...

📝 Insertando matrículas de ejemplo...

🎯 Estudiantes insertados: 3

🎯 Módulos insertados: 4

🎯 Matrículas insertadas: 8

✅ Datos de ejemplo insertados correctamente

4. Verificación en Base de Datos
   Abrimos DBeaaver y podremos ver en nuestra base de datos aad_db las tablas student, module y enrollment con los datos insertados


Estructura de la Base de Datos

student: Almacena información de estudiantes

module: Almacena información de módulos/cursos

enrollment: Gestiona las matrículas de estudiantes en módulos

Powered by AAD 3.5.5
2025-11-29T16:29:23.689+01:00  INFO 30232 --- [           main] com.farrnav3006.aad.Application          : Starting Application using Java 25 with PID 30232 (C:\Users\PepsiPaco\IdeaProjects\AAD_25_26\target\classes started by PepsiPaco in C:\Users\PepsiPaco\IdeaProjects\AAD_25_26)
2025-11-29T16:29:23.692+01:00  INFO 30232 --- [           main] com.farrnav3006.aad.Application          : No active profile set, falling back to 1 default profile: "default"
2025-11-29T16:29:24.688+01:00  INFO 30232 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-11-29T16:29:24.702+01:00  INFO 30232 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-11-29T16:29:24.702+01:00  INFO 30232 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.44]
2025-11-29T16:29:24.752+01:00  INFO 30232 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-11-29T16:29:24.753+01:00  INFO 30232 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 998 ms
2025-11-29T16:29:25.319+01:00  INFO 30232 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-11-29T16:29:25.327+01:00  INFO 30232 --- [           main] com.farrnav3006.aad.Application          : Started Application in 2.13 seconds (process running for 2.481)
2025-11-29T16:29:25.334+01:00  INFO 30232 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-11-29T16:29:25.481+01:00  INFO 30232 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@60cb1ed6
2025-11-29T16:29:25.482+01:00  INFO 30232 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2025-11-29T16:29:25.507+01:00  INFO 30232 --- [           main] c.f.aad.repository.StudentRepository     : create OK: Student(id=1, nif=66280457T, name=Miriam, email=miriam@g.educaand.es)
2025-11-29T16:29:25.508+01:00  INFO 30232 --- [           main] c.f.a.a.StudentManagementService         : Student created with ID: 1
2025-11-29T16:29:25.512+01:00  INFO 30232 --- [           main] c.f.aad.repository.ModuleRepository      : create OK: Module(id=1, code=0485, name=Programación, hours=250)
2025-11-29T16:29:25.512+01:00  INFO 30232 --- [           main] c.f.a.a.StudentManagementService         : Module created with ID: 1
2025-11-29T16:29:25.523+01:00  INFO 30232 --- [           main] c.f.aad.repository.StudentRepository     : FindById Students OK id=1
2025-11-29T16:29:25.525+01:00  INFO 30232 --- [           main] c.f.aad.repository.ModuleRepository      : FindById Modules OK id=1
2025-11-29T16:29:25.526+01:00  INFO 30232 --- [           main] c.f.aad.repository.EnrollmentRepository  : Creating enrollment - Student ID: 1, Module ID: 1, Date: 2025-11-29
2025-11-29T16:29:25.533+01:00  INFO 30232 --- [           main] c.f.aad.repository.EnrollmentRepository  : Enrollment created successfully
2025-11-29T16:29:25.545+01:00  INFO 30232 --- [           main] c.f.aad.repository.StudentRepository     : Delete Students OK id=1