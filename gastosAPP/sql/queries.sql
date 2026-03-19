-- ============================================
-- GastosApp — Consultas del proyecto
-- Módulo: Bases de Datos (0484)
-- Autor: Tu nombre aquí
-- ============================================

USE gastosapp;

-- ============================================
-- 1. Listado completo de gastos de un usuario
--    con nombre de categoría (JOIN)
-- ============================================
SELECT
    g.id_gasto,
    g.fecha,
    c.nombre        AS categoria,
    g.descripcion,
    g.importe
FROM GASTO g
JOIN CATEGORIA c ON g.id_categoria = c.id_categoria
WHERE g.id_usuario = 1
ORDER BY g.fecha DESC;

-- ============================================
-- 2. Total gastado por mes (resumen mensual)
-- ============================================
SELECT
    YEAR(fecha)     AS anio,
    MONTH(fecha)    AS mes,
    COUNT(*)        AS num_gastos,
    SUM(importe)    AS total_gastado
FROM GASTO
WHERE id_usuario = 1
GROUP BY YEAR(fecha), MONTH(fecha)
ORDER BY anio DESC, mes DESC;

-- ============================================
-- 3. Total gastado por categoría en un mes
-- ============================================
SELECT
    c.nombre        AS categoria,
    COUNT(*)        AS num_gastos,
    SUM(g.importe)  AS total
FROM GASTO g
JOIN CATEGORIA c ON g.id_categoria = c.id_categoria
WHERE g.id_usuario = 1
  AND MONTH(g.fecha) = 1
  AND YEAR(g.fecha)  = 2025
GROUP BY c.id_categoria, c.nombre
ORDER BY total DESC;

-- ============================================
-- 4. Filtrar gastos por categoría
-- ============================================
SELECT
    g.fecha,
    g.descripcion,
    g.importe
FROM GASTO g
JOIN CATEGORIA c ON g.id_categoria = c.id_categoria
WHERE g.id_usuario = 1
  AND c.nombre = 'Alimentación'
ORDER BY g.fecha DESC;

-- ============================================
-- 5. Comparar gasto real vs presupuesto por mes
--    (alerta si se supera el límite)
-- ============================================
SELECT
    p.mes,
    p.anio,
    p.limite,
    COALESCE(SUM(g.importe), 0)             AS total_gastado,
    p.limite - COALESCE(SUM(g.importe), 0)  AS diferencia,
    CASE
        WHEN COALESCE(SUM(g.importe), 0) > p.limite THEN '🔴 SUPERADO'
        WHEN COALESCE(SUM(g.importe), 0) > p.limite * 0.8 THEN '🟡 CERCA DEL LÍMITE'
        ELSE '🟢 OK'
    END AS estado
FROM PRESUPUESTO p
LEFT JOIN GASTO g
    ON  g.id_usuario = p.id_usuario
    AND MONTH(g.fecha) = p.mes
    AND YEAR(g.fecha)  = p.anio
WHERE p.id_usuario   = 1
  AND p.id_categoria IS NULL
GROUP BY p.id_presupuesto, p.mes, p.anio, p.limite
ORDER BY p.anio DESC, p.mes DESC;

-- ============================================
-- 6. Buscar gastos por descripción (búsqueda)
-- ============================================
SELECT
    g.fecha,
    c.nombre    AS categoria,
    g.descripcion,
    g.importe
FROM GASTO g
JOIN CATEGORIA c ON g.id_categoria = c.id_categoria
WHERE g.id_usuario  = 1
  AND g.descripcion LIKE '%Mercadona%'
ORDER BY g.fecha DESC;

-- ============================================
-- 7. Top 5 categorías donde más se gasta
-- ============================================
SELECT
    c.nombre        AS categoria,
    SUM(g.importe)  AS total,
    COUNT(*)        AS num_transacciones
FROM GASTO g
JOIN CATEGORIA c ON g.id_categoria = c.id_categoria
WHERE g.id_usuario = 1
GROUP BY c.id_categoria, c.nombre
ORDER BY total DESC
LIMIT 5;

-- ============================================
-- 8. Gastos entre dos fechas
-- ============================================
SELECT
    g.fecha,
    c.nombre    AS categoria,
    g.descripcion,
    g.importe
FROM GASTO g
JOIN CATEGORIA c ON g.id_categoria = c.id_categoria
WHERE g.id_usuario = 1
  AND g.fecha BETWEEN '2025-01-01' AND '2025-01-31'
ORDER BY g.fecha;
