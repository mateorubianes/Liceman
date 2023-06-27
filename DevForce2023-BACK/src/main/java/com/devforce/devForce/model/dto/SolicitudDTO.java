package com.devforce.devForce.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SolicitudDTO {

    private long id;
    private String tipo;
    private String descripcion;
    private String estado;
    private String area;
    private String link;
    private int apruebaMentorID;
    private int apruebaAdminID;
    private int tiempoSolicitado;
    private UsuarioDTO usuario;
}
