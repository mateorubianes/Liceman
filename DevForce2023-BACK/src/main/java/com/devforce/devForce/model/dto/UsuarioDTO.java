package com.devforce.devForce.model.dto;

import com.devforce.devForce.model.entity.Role;
import com.devforce.devForce.model.entity.Solicitud;
import com.devforce.devForce.model.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    //Atributos
    private long id;
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String phone;
    private Boolean hasTeams;
    private String mentorArea;
    private Set<Role> roles;
    //TODO: AGREGAR ROLES

    //Constructor
    //TODO: CAMBIAR AL SERVICIO DEL USUARIO
    /*
        public UsuarioDTO (Usuario usuario)
        {
            this.id = usuario.getId();
            this.nombre = usuario.getNombre();
            this.apellido = usuario.getApellido();
            this.username = usuario.getUsername();
            this.email = usuario.getEmail();
            this.phone = usuario.getPhone();
            this.hasTeams = usuario.getHasTeams();
            this.mentorArea = usuario.getMentorArea();
            this.solicitudes = usuario.getSolicitudes().stream().map(SolicitudDTO::new).collect(Collectors.toList());
        }
    */
}
