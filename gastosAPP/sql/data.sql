-- ============================================
-- GastosApp — Script de inserción de datos de ejemplo
-- Módulo: Bases de Datos (0484)
-- Autor: Tu nombre aquí
-- ============================================

USE gastosapp;

-- ============================================
-- USUARIOS
-- ============================================
INSERT INTO USUARIO (nombre, email, fecha_registro) VALUES
    ('Alejandro Criado',    'alejandro@email.com',  '2025-01-10'),
    ('María López',         'maria@email.com',      '2025-01-15'),
    ('Carlos Fernández',    'carlos@email.com',     '2025-02-01');

-- ============================================
-- CATEGORIAS
-- ============================================
INSERT INTO CATEGORIA (nombre, descripcion) VALUES
    ('Alimentación',    'Supermercado, restaurantes y comida en general'),
    ('Transporte',      'Gasolina, transporte público, taxi, etc.'),
    ('Ocio',            'Cine, conciertos, salidas, entretenimiento'),
    ('Salud',           'Farmacia, médico, gimnasio'),
    ('Hogar',           'Alquiler, luz, agua, internet, limpieza'),
    ('Ropa',            'Ropa, calzado y accesorios'),
    ('Tecnología',      'Dispositivos, suscripciones digitales, apps'),
    ('Otros',           'Gastos no clasificados en otras categorías');

-- ============================================
-- GASTOS — Enero 2025
-- ============================================
INSERT INTO GASTO (id_usuario, id_categoria, importe, descripcion, fecha) VALUES
    (1, 1, 85.30,   'Compra semanal Mercadona',         '2025-01-05'),
    (1, 2, 50.00,   'Abono transporte mensual',         '2025-01-07'),
    (1, 3, 12.50,   'Cine con amigos',                  '2025-01-10'),
    (1, 5, 650.00,  'Alquiler enero',                   '2025-01-01'),
    (1, 1, 42.10,   'Compra supermercado',              '2025-01-14'),
    (1, 7, 9.99,    'Suscripción Netflix',              '2025-01-15'),
    (1, 4, 18.50,   'Farmacia',                         '2025-01-18'),
    (1, 1, 67.80,   'Compra semanal Mercadona',         '2025-01-21'),
    (1, 3, 45.00,   'Cena restaurante',                 '2025-01-25'),
    (1, 6, 35.99,   'Camisetas Zara',                   '2025-01-28'),

-- ============================================
-- GASTOS — Febrero 2025
-- ============================================
    (1, 5, 650.00,  'Alquiler febrero',                 '2025-02-01'),
    (1, 1, 91.20,   'Compra supermercado',              '2025-02-03'),
    (1, 2, 50.00,   'Abono transporte mensual',         '2025-02-07'),
    (1, 7, 9.99,    'Suscripción Netflix',              '2025-02-15'),
    (1, 3, 60.00,   'Concierto',                        '2025-02-20'),
    (1, 4, 25.00,   'Médico privado',                   '2025-02-22'),
    (1, 1, 55.40,   'Compra semanal',                   '2025-02-25'),

-- ============================================
-- GASTOS — Marzo 2025
-- ============================================
    (1, 5, 650.00,  'Alquiler marzo',                   '2025-03-01'),
    (1, 1, 78.60,   'Compra supermercado',              '2025-03-04'),
    (1, 2, 50.00,   'Abono transporte mensual',         '2025-03-07'),
    (1, 7, 9.99,    'Suscripción Netflix',              '2025-03-15'),
    (1, 6, 89.95,   'Zapatillas Nike',                  '2025-03-18'),
    (1, 3, 30.00,   'Salida fin de semana',             '2025-03-22'),
    (1, 1, 62.30,   'Compra semanal',                   '2025-03-26'),

-- USUARIO 2
    (2, 1, 110.50,  'Compra mensual',                   '2025-01-08'),
    (2, 2, 30.00,   'Gasolina',                         '2025-01-12'),
    (2, 5, 800.00,  'Alquiler enero',                   '2025-01-01'),
    (2, 3, 25.00,   'Streaming y entretenimiento',      '2025-01-20');

-- ============================================
-- PRESUPUESTOS
-- ============================================
INSERT INTO PRESUPUESTO (id_usuario, id_categoria, limite, mes, anio) VALUES
    -- Presupuesto global mensual usuario 1
    (1, NULL,   1200.00,    1, 2025),
    (1, NULL,   1200.00,    2, 2025),
    (1, NULL,   1200.00,    3, 2025),
    -- Presupuesto por categoría usuario 1
    (1, 1,      200.00,     1, 2025),
    (1, 1,      200.00,     2, 2025),
    (1, 1,      200.00,     3, 2025),
    (1, 3,      80.00,      1, 2025),
    (1, 3,      80.00,      2, 2025),
    (1, 3,      80.00,      3, 2025),
    -- Presupuesto global usuario 2
    (2, NULL,   1500.00,    1, 2025);
