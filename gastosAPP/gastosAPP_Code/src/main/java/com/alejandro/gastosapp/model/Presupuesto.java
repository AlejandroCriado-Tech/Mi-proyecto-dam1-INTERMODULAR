package com.alejandro.gastosapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presupuesto {

    private int    idPresupuesto;  // id_presupuesto — clave primaria
    private int    idUsuario;      // id_usuario     — clave foránea
    private int    idCategoria;    // id_categoria   — clave foránea (0 = global)
    private double limite;         // limite         — importe máximo permitido
    private int    mes;            // mes            — mes al que aplica (1-12)
    private int    anio;           // anio — año al que aplica
}