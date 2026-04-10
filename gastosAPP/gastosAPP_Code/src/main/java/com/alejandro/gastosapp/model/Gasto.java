package com.alejandro.gastosapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Gasto {

    private int       idGasto;      // id_gasto     — clave primaria
    private int       idUsuario;    // id_usuario   — clave foránea
    private int       idCategoria;  // id_categoria — clave foránea
    private double    importe;      // importe      — cantidad gastada
    private String    descripcion;  // descripcion  — texto descriptivo
    private LocalDate fecha;        // fecha — cuándo se hizo
}