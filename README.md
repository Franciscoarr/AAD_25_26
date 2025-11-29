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
