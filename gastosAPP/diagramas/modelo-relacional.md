# Modelo Relacional — GastosApp

Transformación del diagrama E/R a tablas relacionales.

---

## USUARIO

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id_usuario (PK) | INT | AUTO_INCREMENT, NOT NULL |
| nombre | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL, UNIQUE |
| fecha_registro | DATE | NOT NULL, DEFAULT CURRENT_DATE |

---

## CATEGORIA

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id_categoria (PK) | INT | AUTO_INCREMENT, NOT NULL |
| nombre | VARCHAR(50) | NOT NULL, UNIQUE |
| descripcion | VARCHAR(200) | NULL |

---

## GASTO

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id_gasto (PK) | INT | AUTO_INCREMENT, NOT NULL |
| id_usuario (FK) | INT | NOT NULL → USUARIO(id_usuario) |
| id_categoria (FK) | INT | NOT NULL → CATEGORIA(id_categoria) |
| importe | DECIMAL(10,2) | NOT NULL, CHECK importe > 0 |
| descripcion | VARCHAR(200) | NULL |
| fecha | DATE | NOT NULL |

---

## PRESUPUESTO

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id_presupuesto (PK) | INT | AUTO_INCREMENT, NOT NULL |
| id_usuario (FK) | INT | NOT NULL → USUARIO(id_usuario) |
| id_categoria (FK) | INT | NULL → CATEGORIA(id_categoria) |
| limite | DECIMAL(10,2) | NOT NULL, CHECK limite > 0 |
| mes | TINYINT | NOT NULL, CHECK mes BETWEEN 1 AND 12 |
| anio | YEAR | NOT NULL |

> 💡 `id_categoria NULL` esto es porque un presupuesto puede ser global (para todo el mes) o específico de una categoría.

---

## Relaciones

| Tabla origen | Campo | Tabla destino | Cardinalidad |
|-------------|-------|---------------|-------------|
| GASTO | id_usuario | USUARIO | N:1 |
| GASTO | id_categoria | CATEGORIA | N:1 |
| PRESUPUESTO | id_usuario | USUARIO | N:1 |
| PRESUPUESTO | id_categoria | CATEGORIA | N:1 (opcional) |
