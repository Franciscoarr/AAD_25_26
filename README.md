## Conector y su papel

Un conector es el componente que permite que la aplicación se comunique con la base de datos. Gestiona las conexiones,
envía consultas y recibe los resultados.
Actúa como un puente entre la app y PostgreSQL.

## Levantar servicios PostgreSQL

Se utiliza Docker Compose para levantar los servicios de PostgreSQL "docker-compose up -d" en donde se encuentre el
fichero .yml.

## Variables utilizados

En el .yml, utilizaremos POSTGRES_DB: aad_db (nombre de la base de datos); POSTGRES_USER: user (nombre del usuario) y
POSTGRES_PASSWORD: pass (contraseña del usuario).
También usaremos el puerto 5432 u otro para conectarnos.
Estas variables se utilizan para configurar la conexión a la base de datos desde DBeaver introduciendo los mismos
valores en la configuración de la conexión.

## Probar la conexión

Para probar la conexión, podemos hacer durante la creación un test para verificar que los datos son correctos y así la
conexión se hará correctamente, y ya conectar la base de datos luego de la comprobación.