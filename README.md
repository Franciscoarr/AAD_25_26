## Funcionamiento
* Agregar eventos: Añade mensajes con timestamp automático

* Filtrar eventos: Busca eventos por fecha específica

* Cambiar codificación: Soporte para UTF-8 e ISO-8859-1

Los logs se guardan en formato: [YYYY/MM/DD HH:MM:SS] + mensaje

## Requisitos

Se requiere las dependencias de Springboot como Lombok y mínimo Java 8

## Ejemplo de uso
*Menú principal:*

===== Log Manager =====
1. Add events
2. Filter events
3. Change encoding
4. Exit
   
*Agregar evento:*

Enter the event message: Ejemplo de uso README -->
Event added successfully

Enter the event message: "" -->
Message cannot be empty

*Filtrar eventos:*

Enter event date (YYYY/MM/DD): 2025/10/25 -->
[2025/10/25 10:30:45] Ejemplo de uso README

Enter event date (YYYY/MM/DD): 2025/10/24 -->
No events found for that date

*Cambiar codificación*

===== Change encoding =====
1. UTF-8"
2. ISO-8859-1"
3. Exit to main menu

Encoding changed to UTF-8 o Encoding changed to ISO-8859-1

*Archivo de log:*

[2025/10/25 10:30:45] Ejemplo de uso README

[2025/10/25 11:20:15] Segundo Ejemplo de uso README

[2025/10/25 11:28:15] Tercer Ejemplo de uso README
