# 💰 GastosApp — Gestor de Gastos Personales

> Proyecto Intermodular de 1º — Técnico Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)  
> Prometeo by The Power Education

---

## 📋 Descripción del proyecto

**GastosApp** es una aplicación de escritorio desarrollada en Java que permite a cualquier usuario llevar un control claro y organizado de sus gastos personales.

El problema que resuelve es sencillo pero muy real: la mayoría de las personas no sabe exactamente en qué gasta su dinero cada mes. Esta app permite registrar cada gasto, categorizarlo, filtrar por fecha o tipo, y ver resúmenes mensuales con alertas si se supera el presupuesto establecido.

---

## 🎯 Funcionalidades principales

- **Gestión de gastos** — añadir, editar y eliminar gastos con descripción, importe, categoría y fecha
- **Listado y filtros** — consultar gastos filtrando por fecha o por categoría
- **Resumen mensual** — ver el total gastado por mes y compararlo con el presupuesto
- **Presupuesto y alertas** — establecer un límite mensual y recibir aviso al superarlo
- **Interfaz por consola** — menú interactivo navegable desde el terminal

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|------------|-----|
| Java 17 | Lenguaje principal de desarrollo |
| JDBC | Conexión y operaciones con la base de datos |
| MySQL | Sistema gestor de base de datos |
| XML / XSD | Exportación de datos y validación de estructura |
| Git / GitHub | Control de versiones |

---

## 🗂️ Estructura del repositorio

```
gastosapp/
│
├── src/                        # Código fuente Java
│   ├── model/                  # Clases de dominio (Gasto, Categoria, Presupuesto)
│   ├── service/                # Lógica de negocio
│   ├── controller/             # Gestión del flujo de la app
│   └── Main.java               # Punto de entrada
│
├── sql/                        # Scripts de base de datos
│   ├── schema.sql              # Creación de tablas
│   ├── data.sql                # Datos de ejemplo
│   └── queries.sql             # Consultas del proyecto
│
├── xml/                        # Módulo Lenguajes de Marcas
│   ├── datos.xml               # Exportación de gastos en XML
│   └── esquema.xsd             # Esquema de validación
│
├── docs/                       # Documentación
│   └── sistemas/               # Informe técnico del entorno (SSII)
│
├── diagrams/                   # Diagramas del proyecto
│   ├── diagrama-er.png         # Diagrama Entidad-Relación
│   └── modelo-relacional.md    # Modelo relacional en texto
│
└── README.md                   # Este archivo
```

---

## ⚙️ Instalación y ejecución

### Requisitos previos

- Java 17 o superior instalado
- MySQL 8.0 o superior
- IDE recomendado: IntelliJ IDEA, Antigravity o Eclipse

### Pasos

**1. Clona el repositorio**
```bash
git clone https://github.com/AlejandroCriado-Tech/Mi-proyecto-dam1-INTERMODULAR.git
```

**2. Crea la base de datos**
```bash
mysql -u root -p
```
```sql
CREATE DATABASE gastosapp;
USE gastosapp;
SOURCE sql/schema.sql;
SOURCE sql/data.sql;
```

**3. Configura la conexión**

Edita el archivo `src/service/ConexionDB.java` con tus credenciales:
```java
private static final String URL = "jdbc:mysql://localhost:3306/gastosapp";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

**4. Compila y ejecuta**
```bash
javac -cp . src/Main.java
java Main
```

---

## 🗃️ Base de datos

El modelo de datos incluye las siguientes entidades principales:

- **Gasto** — importe, descripción, fecha, categoría
- **Categoría** — nombre, color o icono representativo
- **Presupuesto** — límite mensual establecido por el usuario

El diagrama E/R completo se encuentra en `/diagrams/diagrama-er.png`.

---

## 📦 Módulos del proyecto intermodular

| Módulo | Entregable en este repo |
|--------|------------------------|
| Bases de Datos (0484) | `/sql/` + `/diagrams/` |
| Entornos de Desarrollo (0487) | Repositorio, commits, este README |
| Lenguajes de Marcas (0373) | `/xml/` |
| Programación (0485) | `/src/` |
| Sistemas Informáticos (0483) | `/docs/sistemas/` |
| MPO – Ampliación de Programación | `/src/` (arquitectura en capas) |

---

## 👤 Autor

- **Nombre:** Tu nombre aquí
- **GitHub:** [@AlejandroCriado-tech](https://github.com/AlejandroCriado-tech)
- **Centro:** Prometeo by The Power Education — DAM Virtual, 1º curso
- **Período:** Marzo – Mayo 2025

---

## 📅 Estado del proyecto

🟡 En desarrollo — Fase 1: Diseño de base de datos y estructura inicial
