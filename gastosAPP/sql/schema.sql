-- ============================================
-- GastosApp — Script de creación de base de datos
-- Módulo: Bases de Datos (0484)
-- Autor: Tu nombre aquí
-- ============================================

CREATE DATABASE IF NOT EXISTS gastosapp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE gastosapp;

-- ============================================
-- TABLA: USUARIO
-- ============================================
CREATE TABLE IF NOT EXISTS USUARIO (
    id_usuario      INT             NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    fecha_registro  DATE            NOT NULL DEFAULT (CURRENT_DATE),
    CONSTRAINT pk_usuario   PRIMARY KEY (id_usuario),
    CONSTRAINT uq_email     UNIQUE (email)
);

-- ============================================
-- TABLA: CATEGORIA
-- ============================================
CREATE TABLE IF NOT EXISTS CATEGORIA (
    id_categoria    INT             NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(50)     NOT NULL,
    descripcion     VARCHAR(200)    NULL,
    CONSTRAINT pk_categoria     PRIMARY KEY (id_categoria),
    CONSTRAINT uq_cat_nombre    UNIQUE (nombre)
);

-- ============================================
-- TABLA: GASTO
-- ============================================
CREATE TABLE IF NOT EXISTS GASTO (
    id_gasto        INT             NOT NULL AUTO_INCREMENT,
    id_usuario      INT             NOT NULL,
    id_categoria    INT             NOT NULL,
    importe         DECIMAL(10,2)   NOT NULL,
    descripcion     VARCHAR(200)    NULL,
    fecha           DATE            NOT NULL,
    CONSTRAINT pk_gasto         PRIMARY KEY (id_gasto),
    CONSTRAINT fk_gasto_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_gasto_categoria FOREIGN KEY (id_categoria)
        REFERENCES CATEGORIA(id_categoria)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT chk_importe      CHECK (importe > 0)
);

-- ============================================
-- TABLA: PRESUPUESTO
-- ============================================
CREATE TABLE IF NOT EXISTS PRESUPUESTO (
    id_presupuesto  INT             NOT NULL AUTO_INCREMENT,
    id_usuario      INT             NOT NULL,
    id_categoria    INT             NULL,
    limite          DECIMAL(10,2)   NOT NULL,
    mes             TINYINT         NOT NULL,
    anio            YEAR            NOT NULL,
    CONSTRAINT pk_presupuesto           PRIMARY KEY (id_presupuesto),
    CONSTRAINT fk_presupuesto_usuario   FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_presupuesto_categoria FOREIGN KEY (id_categoria)
        REFERENCES CATEGORIA(id_categoria)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT chk_limite   CHECK (limite > 0),
    CONSTRAINT chk_mes      CHECK (mes BETWEEN 1 AND 12)
);
