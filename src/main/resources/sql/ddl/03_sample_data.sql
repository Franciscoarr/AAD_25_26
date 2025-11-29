INSERT INTO alumno (nif, nombre, email) VALUES
('12345678A', 'Juan Pérez', 'juan.perez@example.com'),
('87654321B', 'María López', 'marialopez@example.com'),
('11223344C', 'Carlos Sánchez', 'carlossanchez@example.com'),
('44332211D', 'Lucía Martínez', 'luciamartinez@example.com');

-- ---------- MÓDULOS ----------
INSERT INTO modulo (codigo, nombre, horas) VALUES
('0800', 'Programación Básica', 120),
('0700', 'Bases de Datos', 100),
('0660', 'Sistemas Informáticos', 90),
('0750', 'Entornos de Desarrollo', 80),
('0680', 'Lenguajes de Marcas', 70);

-- ---------- MATRÍCULAS ----------
-- Juan Pérez (id_alumno = 1)
INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
(1, 1, '2024-09-01'),
(1, 2, '2024-09-01'),
(1, 3, '2024-09-02');

-- María López (id_alumno = 2)
INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
(2, 1, '2024-09-05'),
(2, 4, '2024-09-05');

-- Carlos Sánchez (id_alumno = 3)
INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
(3, 2, '2024-09-10');

-- Lucía Martínez (id_alumno = 4)
INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES
(4, 3, '2024-09-12'),
(4, 5, '2024-09-12');