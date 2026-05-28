# Diagrama Entidad-Relación — GastosApp

```mermaid
erDiagram

    USUARIO {
        INT     id_usuario      PK  "AUTO_INCREMENT"
        VARCHAR nombre          "NOT NULL"
        VARCHAR email           "NOT NULL, UNIQUE"
        DATE    fecha_registro  "NOT NULL, DEFAULT CURRENT_DATE"
    }

    CATEGORIA {
        INT     id_categoria    PK  "AUTO_INCREMENT"
        VARCHAR nombre          "NOT NULL, UNIQUE"
        VARCHAR descripcion     "NULL"
    }

    GASTO {
        INT         id_gasto        PK  "AUTO_INCREMENT"
        INT         id_usuario      FK  "NOT NULL"
        INT         id_categoria    FK  "NOT NULL"
        DECIMAL     importe         "NOT NULL, > 0"
        VARCHAR     descripcion     "NULL"
        DATE        fecha           "NOT NULL"
    }

    PRESUPUESTO {
        INT     id_presupuesto  PK  "AUTO_INCREMENT"
        INT     id_usuario      FK  "NOT NULL"
        INT     id_categoria    FK  "NULL (global si NULL)"
        DECIMAL limite          "NOT NULL, > 0"
        TINYINT mes             "NOT NULL, 1–12"
        YEAR    anio            "NOT NULL"
    }

    USUARIO ||--o{ GASTO        : "registra"
    CATEGORIA ||--o{ GASTO      : "clasifica"
    USUARIO ||--o{ PRESUPUESTO  : "define"
    CATEGORIA |o--o{ PRESUPUESTO : "acota (opcional)"
```

---

## Descripción de relaciones

| Relación | Cardinalidad | Detalle |
|---|---|---|
| USUARIO → GASTO | 1 : N | Un usuario puede tener muchos gastos. `ON DELETE CASCADE` |
| CATEGORIA → GASTO | 1 : N | Cada gasto pertenece a una categoría. `ON DELETE RESTRICT` |
| USUARIO → PRESUPUESTO | 1 : N | Un usuario puede tener varios presupuestos. `ON DELETE CASCADE` |
| CATEGORIA → PRESUPUESTO | 0..1 : N | La categoría es opcional: `NULL` indica presupuesto global mensual. `ON DELETE SET NULL` |

---

## Notas de integridad

- **`GASTO.importe`** — restricción `CHECK (importe > 0)`.
- **`PRESUPUESTO.limite`** — restricción `CHECK (limite > 0)`.
- **`PRESUPUESTO.mes`** — restricción `CHECK (mes BETWEEN 1 AND 12)`.
- **`USUARIO.email`** — restricción `UNIQUE`.
- **`CATEGORIA.nombre`** — restricción `UNIQUE`.
- La combinación `PRESUPUESTO(id_usuario, id_categoria = NULL, mes, anio)` representa el **presupuesto global** del usuario para ese mes.
