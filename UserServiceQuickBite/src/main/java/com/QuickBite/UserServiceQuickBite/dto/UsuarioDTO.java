package com.QuickBite.UserServiceQuickBite.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioDTO {

    private UUID id;
    private String nombre;
    private String apellido;
    private Integer telefono;
    private String direccion;
    private String ciudad;
    private Date fechaRegistro;


}
