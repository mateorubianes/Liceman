package com.devforce.devForce.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RespuestaDTO {

    private boolean ok;
    private String mensaje;
    private Object contenido;
}

