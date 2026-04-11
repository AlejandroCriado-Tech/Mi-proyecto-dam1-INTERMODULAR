# Informe Técnico de Entorno de Ejecución — GastosApp

**Módulo:** Sistemas Informáticos (0483)  
**Autor:** Alejandro Criado Pérez  
**Fecha:** Abril 2026  
**Proyecto:** GastosApp — Gestor de Gastos Personales  

---

## 1. Tipo de sistema donde se ejecuta

GastosApp es una aplicación de escritorio pensada para ejecutarse en un **PC de usuario estándar**. No requiere servidor dedicado ni infraestructura especial, ya que tanto la aplicación como la base de datos corren en la misma máquina local.

Esta decisión es coherente con el objetivo del proyecto: una app personal de gestión de gastos que usa un único usuario y no necesita acceso remoto ni concurrencia de múltiples conexiones.

---

## 2. Requisitos de hardware

### Mínimos
| Componente | Especificación mínima |
|-----------|----------------------|
| CPU | Intel Core i3 / AMD Ryzen 3 (2 núcleos, 1.5 GHz) |
| RAM | 4 GB |
| Almacenamiento | 500 MB libres |
| Pantalla | Resolución 1024x768 |
| Conexión | No requiere internet |

### Recomendados
| Componente | Especificación recomendada |
|-----------|--------------------------|
| CPU | Intel Core i5 / AMD Ryzen 5 (4 núcleos, 2.5 GHz) |
| RAM | 8 GB |
| Almacenamiento | 2 GB libres (para logs y copias de seguridad) |
| Pantalla | Resolución 1920x1080 |

Los requisitos son bajos porque la app no realiza operaciones pesadas — simplemente gestiona registros en una base de datos local y muestra una interfaz gráfica sencilla con JavaFX.

---

## 3. Sistema operativo recomendado

**Sistema operativo principal:** Windows 10 / Windows 11 (64 bits)

**Versión recomendada:** Windows 11 Home o Pro, versión 22H2 o superior.

**¿Por qué Windows y no otro?**

- Es el sistema operativo más extendido en equipos de uso personal y doméstico, que es el público objetivo de GastosApp.
- Java 25 y JavaFX 25 tienen soporte oficial y estable para Windows.
- XAMPP, que gestiona el servidor MySQL local, está optimizado para Windows.
- La experiencia de instalación es más sencilla para usuarios no técnicos.

La aplicación también es compatible con **Linux** (Ubuntu 20+) y **macOS** (12+), ya que Java es multiplataforma, pero el entorno de desarrollo y pruebas se ha realizado íntegramente en Windows.

---

## 4. Instalación del entorno paso a paso

### Paso 1 — Instalar Java 25

1. Ve a [https://openjdk.org](https://openjdk.org) y descarga **OpenJDK 25**
2. Ejecuta el instalador y sigue los pasos
3. Verifica la instalación abriendo una terminal (cmd) y escribiendo:
```bash
java -version
```
Debe mostrar: `openjdk version "25"`

### Paso 2 — Instalar XAMPP

1. Ve a [https://apachefriends.org](https://apachefriends.org) y descarga XAMPP para Windows
2. Instala con las opciones por defecto — asegúrate de que **MySQL** esté marcado
3. Abre el panel de control de XAMPP y pulsa **Start** en MySQL
4. Verifica que MySQL está en verde

### Paso 3 — Crear la base de datos

1. Abre el navegador y ve a `http://localhost/phpmyadmin`
2. Crea una nueva base de datos llamada `gastosapp` con cotejamiento `utf8mb4_spanish_ci`
3. Selecciona `gastosapp` y ve a la pestaña **SQL**
4. Ejecuta el contenido de `sql/schema.sql` para crear las tablas
5. Ejecuta el contenido de `sql/data.sql` para insertar los datos de ejemplo

### Paso 4 — Ejecutar la aplicación

1. Abre la carpeta `gastosAPP/gastosAPP_Code/` del repositorio con IntelliJ IDEA
2. Espera a que Maven descargue las dependencias automáticamente
3. Asegúrate de que XAMPP tiene MySQL en verde
4. Ejecuta la clase `Main.java` dentro de `src/main/java/.../view/`
5. La ventana principal de GastosApp se abrirá

---

## 5. Usuarios, permisos y estructura

### Usuarios del sistema

| Usuario | Rol | Permisos |
|---------|-----|----------|
| root | Administrador MySQL | Acceso total a la base de datos |
| (usuario del SO) | Usuario de la app | Ejecutar la aplicación |

### Estructura de carpetas relevante

```
gastosAPP/
├── gastosAPP_Code/         → Código fuente del proyecto Java
│   ├── src/                → Código fuente
│   └── target/             → Archivos compilados (generados por Maven)
├── sql/                    → Scripts de base de datos
├── xml/                    → Archivos XML y XSD
└── docs/                   → Documentación e informes
```

### Base de datos

- **Motor:** MySQL 8.0 (gestionado por XAMPP)
- **Puerto:** 3306 (por defecto) o 3307
- **Usuario:** root
- **Contraseña:** vacía (entorno local de desarrollo)
- **Base de datos:** gastosapp

---

## 6. Mantenimiento básico

### ¿Qué se debería actualizar?

| Componente | Frecuencia | Acción |
|-----------|-----------|--------|
| Java (OpenJDK) | Cada 6 meses | Actualizar a la última versión estable |
| XAMPP / MySQL | Anual | Actualizar si hay parches de seguridad |
| Dependencias Maven | Por proyecto | Revisar versiones en pom.xml |

### ¿Qué se debería revisar?

- Comprobar que XAMPP arranca correctamente con cada inicio del sistema
- Verificar que la base de datos no ha crecido de forma anormal
- Revisar los logs de la consola de IntelliJ por si aparecen errores de conexión

### ¿Qué hacer si falla?

| Problema | Solución |
|---------|---------|
| "Communications link failure" | MySQL no está arrancado — abrir XAMPP y pulsar Start en MySQL |
| "Location is not set" | Los archivos .fxml no están en resources — revisar estructura del proyecto |
| La app no arranca | Verificar que Java 25 está instalado correctamente con `java -version` |
| Error en BD | Revisar credenciales en `ConexionDB.java` (URL, usuario, contraseña) |

### Copias de seguridad

Para hacer una copia de seguridad de la base de datos, desde phpMyAdmin:
1. Selecciona `gastosapp`
2. Ve a **Exportar**
3. Pulsa **Continuar** — descargará un archivo `.sql` con todos los datos

Se recomienda hacer esta copia una vez al mes y guardarla en una carpeta segura.

---

## 7. Evidencias de funcionamiento

Las capturas de funcionamiento de la aplicación se encuentran en la carpeta `/docs/sistemas/` del repositorio e incluyen:

- Pantalla principal de GastosApp arrancada
- Pantalla de gestión de gastos con datos cargados desde MySQL
- Pantalla de presupuestos con resumen mensual y alerta de estado
- Conexión activa con la base de datos confirmada en consola

---

## Conclusión

GastosApp se ejecuta correctamente en un PC de usuario estándar con Windows 10/11, Java 25 y MySQL gestionado por XAMPP. El entorno es sencillo de instalar, no requiere conexión a internet y está pensado para un uso personal y local. La arquitectura MVC del código y la separación en capas facilita el mantenimiento y la escalabilidad futura del proyecto.
