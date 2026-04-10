package com.alejandro.gastosapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private int       idUsuario;       // id_usuario      — clave primaria
    private String    nombre;          // nombre — nombre del usuario
    private String    email;           // email           — correo electrónico
    private LocalDate fechaRegistro;   // fecha_registro  — cuándo se registró
}